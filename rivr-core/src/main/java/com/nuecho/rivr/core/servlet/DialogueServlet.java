/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.channel.synchronous.*;
import com.nuecho.rivr.core.channel.synchronous.step.*;
import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.servlet.session.*;
import com.nuecho.rivr.core.util.*;

/**
 * Abstract servlet interacting with a web client acting as the controller of a
 * {@link SynchronousDialogueChannel}.
 * <p>
 * This abstract servlet must be extended in order to provide a specific
 * implementation. For each session,
 * <ol>
 * <li>the servlet creates the {@link Session} and place it in the
 * {@link SessionContainer}</li>
 * <li>it creates a {@link Dialogue} with a {@link DialogueFactory}</li>
 * <li>it creates a {@link DialogueContext} with a
 * {@link DialogueContextFactory}</li>
 * <li>it creates a {@link SynchronousDialogueChannel} and starts the dialogue
 * upon initial HTTP request</li>
 * <li>it renders the various {@link Step steps} from the dialogue channel into
 * appropriate HTTP responses</li>
 * <li>it translates HTTP requests into {@link InputTurn InputTurns} using the
 * {@link InputTurnFactory}
 * <li>once the dialogue is done, the servlet perform necessary clean-up.</li>
 * </ol>
 * <h3>init args</h3> The following servlet initial arguments are supported:
 * <p/>
 * <dt>com.nuecho.rivr.core.dialogueTimeout
 * <dd>Maximum time for dialogue to produce an {@link OutputTurn}. Value
 * specified must be followed by unit (ms, s, m, h, d, y), e.g. <code>10s</code>
 * for 10 seconds. Default value: <code>10 s</code></dd></dt>
 * <p/>
 * <dt>com.nuecho.rivr.core.controllerTimeout
 * <dd>Maximum time for controller to produce an {@link InputTurn}. Value
 * specified must be followed by unit (ms, s, m, h, d, y), e.g. <code>10s</code>
 * for 10 seconds. Default value: <code>5 m</code></dd></dt>
 * <p/>
 * <dt>com.nuecho.rivr.core.sessionTimeout</dt>
 * <dd>Maximum inactivity time for a session. Value specified must be followed
 * by unit (ms, s, m, h, d, y), e.g. <code>10s</code> for 10 seconds. Default
 * value: <code>30 m</code></dd>
 * <p/>
 * <dt>com.nuecho.rivr.core.sessionScanPeriod</dt>
 * <dd>Time between each scan for dead sessions in the session container. Value
 * specified must be followed by unit (ms, s, m, h, d, y), e.g. <code>10s</code>
 * for 10 seconds. Default value: <code>2 m</code></dd>
 * <p/>
 * <dt>com.nuecho.rivr.core.webappServerSessionTrackingEnabled</dt>
 * <dd>Whether a {@link javax.servlet.http.HttpSession} should be created for
 * each dialogue or not. This is useful for load-balancers using JSESSIONID
 * cookie to enforce server affinity (or stickyness). Value should be
 * <code>true</code> or <code>false</code>. Default value: <code>true</code>.
 * 
 * @param <F> type of {@link FirstTurn}
 * @param <L> type of {@link LastTurn}
 * @param <O> type of {@link OutputTurn}
 * @param <I> type of {@link InputTurn}
 * @param <C> type of {@link DialogueContext}
 * @author Nu Echo Inc.
 */
public abstract class DialogueServlet<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>>
        extends HttpServlet {

    private static final String MDC_KEY_DIALOGUE_ID = "dialogueId";

    private static final String SESSION_LOGGER_NAME = "com.nuecho.rivr.session";
    private static final String DIALOGUE_LOGGER_NAME = "com.nuecho.rivr.dialogue";

    private static final long serialVersionUID = 1L;
    private static final String SESSION_CONTAINER_NAME = "com.nuecho.rivr.sessionContainer";

    private static final String INITIAL_ARGUMENT_PREFIX = "com.nuecho.rivr.core.";
    private static final String INITIAL_ARGUMENT_DIALOGUE_TIMEOUT = INITIAL_ARGUMENT_PREFIX + "dialogueTimeout";
    private static final String INITIAL_ARGUMENT_SESSION_TIMEOUT = INITIAL_ARGUMENT_PREFIX + "sessionTimeout";
    private static final String INITIAL_ARGUMENT_SESSION_SCAN_PERIOD = INITIAL_ARGUMENT_PREFIX + "sessionScanPeriod";
    private static final String INITIAL_ARGUMENT_CONTROLLER_TIMEOUT = INITIAL_ARGUMENT_PREFIX + "controllerTimeout";

    private static final String INITIAL_ARGUMENT_ENABLE_WEBAPP_SERVER_SESSION_TRACKING = INITIAL_ARGUMENT_PREFIX
                                                                                         + "webappServerSessionTrackingEnabled";

    private ErrorHandler<L> mErrorHandler;
    private DialogueFactory<I, O, F, L, C> mDialogueFactory;
    private DialogueContextFactory<C, I, O> mDialogueContextFactory;
    private ILoggerFactory mLoggerFactory;
    private SessionContainer<I, O, F, L, C> mSessionContainer;
    private InputTurnFactory<I, F> mInputTurnFactory;

    private Duration mDialogueTimeout = Duration.seconds(10);
    private Duration mControllerTimeout = Duration.minutes(5);

    private Duration mSessionTimeout = Duration.minutes(30);
    private Duration mSessionScanPeriod = Duration.minutes(2);

    private boolean mWebappServerSessionTrackingEnabled = true;

    /**
     * Performs initialization.
     */
    protected abstract void initDialogueServlet() throws DialogueServletInitializationException;

    /**
     * Performs shutdown.
     */
    protected abstract void destroyDialogueServlet();

    /**
     * Provides the {@link StepRenderer} appropriate for the context.
     */
    protected abstract StepRenderer<I, O, L, C> getStepRenderer(HttpServletRequest request,
                                                                Session<I, O, F, L, C> session);

    /**
     * Initializes the servlet. The first thing done in this method is to call
     * {@link #initDialogueServlet()}. This method is called by the servlet
     * container.
     */
    @Override
    public final void init() throws ServletException {

        try {
            initDialogueServlet();
        } catch (DialogueServletInitializationException exception) {
            throw new ServletException("Unable to initialize dialogue servlet.s", exception);
        }

        if (mLoggerFactory == null) {
            mLoggerFactory = LoggerFactory.getILoggerFactory();
        }

        ensureFieldIsSet(mInputTurnFactory, "InputTurnFactory");
        ensureFieldIsSet(mDialogueFactory, "DialogueFactory");
        ensureFieldIsSet(mDialogueContextFactory, "DialogueContextFactory");
        ensureFieldIsSet(mErrorHandler, "ErrorHandler");

        Logger logger = mLoggerFactory.getLogger(SESSION_LOGGER_NAME);

        mSessionContainer = new SessionContainer<I, O, F, L, C>(logger,
                                                                mSessionTimeout,
                                                                mSessionScanPeriod,
                                                                SESSION_CONTAINER_NAME);

        Duration sessionScanPeriod = getDuration(INITIAL_ARGUMENT_SESSION_SCAN_PERIOD);
        if (sessionScanPeriod != null) {
            setSessionScanPeriod(sessionScanPeriod);
        }

        Duration sessionTimeout = getDuration(INITIAL_ARGUMENT_SESSION_TIMEOUT);
        if (sessionTimeout != null) {
            setSessionTimeout(sessionTimeout);
        }

        Duration dialogueTimeout = getDuration(INITIAL_ARGUMENT_DIALOGUE_TIMEOUT);
        if (dialogueTimeout != null) {
            setDialogueTimeout(dialogueTimeout);
        }

        Duration controllerTimeout = getDuration(INITIAL_ARGUMENT_CONTROLLER_TIMEOUT);
        if (controllerTimeout != null) {
            setControllerTimeout(controllerTimeout);
        }

        Boolean enableWebappServerSessionTracking = getBoolean(INITIAL_ARGUMENT_ENABLE_WEBAPP_SERVER_SESSION_TRACKING);
        if (enableWebappServerSessionTracking != null) {
            setWebappServerSessionTrackingEnabled(mWebappServerSessionTrackingEnabled);
        }

    }

    /**
     * Destroys the servlet. This methods calls
     * {@link #destroyDialogueServlet()}. This method is called by the servlet
     * container.
     */
    @Override
    public void destroy() {
        destroyDialogueServlet();
    }

    private Duration getDuration(String key) throws ServletException {
        ServletConfig servletConfig = getServletConfig();
        String duration = servletConfig.getInitParameter(key);
        if (duration == null) return null;
        try {
            return Duration.parse(duration);
        } catch (IllegalArgumentException exception) {
            throw new ServletException("Unable to parse duration for init-arg '" + key + "'", exception);
        }
    }

    private Boolean getBoolean(String key) throws ServletException {
        ServletConfig servletConfig = getServletConfig();
        String booleanString = servletConfig.getInitParameter(key);
        if (booleanString == null) return null;
        if (booleanString.equalsIgnoreCase("true")) return Boolean.TRUE;
        if (booleanString.equalsIgnoreCase("false")) return Boolean.FALSE;
        throw new ServletException("Unable to parse boolean for init-arg '"
                                   + key
                                   + "'.  Should be 'true' of 'false' but not '"
                                   + booleanString
                                   + "'.");
    }

    private void ensureFieldIsSet(Object fieldValue, String fieldName) {
        if (fieldValue == null) throw new IllegalStateException(fieldName + " is not set.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    public final ILoggerFactory getLoggerFactory() {
        return mLoggerFactory;
    }

    protected void renderOutputTurn(O outputTurn,
                                    HttpServletRequest request,
                                    final HttpServletResponse response,
                                    Session<I, O, F, L, C> session) throws IOException, StepRendererException {
        ServletResponseContent responseContent = getStepRenderer(request, session).createDocumentForOutputTurn(outputTurn,
                                                                                                               request,
                                                                                                               response,
                                                                                                               session.getDialogueContext());
        commitToResponse(response, responseContent);
    }

    protected void renderLastTurn(L result,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  Session<I, O, F, L, C> session) throws IOException, StepRendererException {
        ServletResponseContent responseContent = getStepRenderer(request, session).createDocumentForLastTurn(result,
                                                                                                             request,
                                                                                                             response,
                                                                                                             session.getDialogueContext());
        commitToResponse(response, responseContent);
        session.stop();
    }

    protected void renderError(Throwable error,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Session<I, O, F, L, C> session) throws IOException, StepRendererException {

        L fatalErrorTurn = mErrorHandler.handleError(error);

        ServletResponseContent responseContent = getStepRenderer(request, session).createDocumentForLastTurn(fatalErrorTurn,
                                                                                                             request,
                                                                                                             response,
                                                                                                             session.getDialogueContext());
        commitToResponse(response, responseContent);
        session.stop();
    }

    public final void setInputTurnFactory(InputTurnFactory<I, F> inputTurnFactory) {
        Assert.notNull(inputTurnFactory, "inputTurnFactory");
        mInputTurnFactory = inputTurnFactory;
    }

    public final void setDialogueFactory(DialogueFactory<I, O, F, L, C> dialogueFactory) {
        Assert.notNull(dialogueFactory, "dialogueFactory");
        mDialogueFactory = dialogueFactory;
    }

    public final void setDialogueContextFactory(DialogueContextFactory<C, I, O> dialogueContextFactory) {
        Assert.notNull(dialogueContextFactory, "dialogueContextFactory");
        mDialogueContextFactory = dialogueContextFactory;
    }

    public final void setLoggerFactory(ILoggerFactory loggerFactory) {
        Assert.notNull(loggerFactory, "loggerFactory");
        mLoggerFactory = loggerFactory;
    }

    /**
     * Sets maximum duration the servlet thread can wait for the dialogue
     * response. Cannot be <code>null</code>. A value of Duration.ZERO (or
     * equivalent) means to wait forever.
     */
    public final void setDialogueTimeout(Duration dialogueTimeout) {
        Assert.notNull(dialogueTimeout, "dialogueTimeout");
        mDialogueTimeout = dialogueTimeout;
    }

    /**
     * Sets maximum duration the dialogue thread can wait for the controller
     * response. Cannot be <code>null</code>. A value of Duration.ZERO (or
     * equivalent) means to wait forever.
     */
    public final void setControllerTimeout(Duration controllerTimeout) {
        Assert.notNull(controllerTimeout, "controllerTimeout");
        mControllerTimeout = controllerTimeout;
    }

    public void setSessionTimeout(Duration sessionTimeout) {
        Assert.notNull(sessionTimeout, "sessionTimeout");
        mSessionTimeout = sessionTimeout;
    }

    public void setSessionScanPeriod(Duration sessionScanPeriod) {
        Assert.notNull(sessionScanPeriod, "sessionScanPeriod");
        mSessionScanPeriod = sessionScanPeriod;
    }

    public void setErrorHandler(ErrorHandler<L> errorHandler) {
        Assert.notNull(errorHandler, "errorHandler");
        mErrorHandler = errorHandler;
    }

    public void setWebappServerSessionTrackingEnabled(boolean enableWebappServerSessionTracking) {
        mWebappServerSessionTrackingEnabled = enableWebappServerSessionTracking;
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Session<I, O, F, L, C> session;
        try {
            session = getSession(request);
            MDC.put(MDC_KEY_DIALOGUE_ID, session.getId());
        } catch (SessionNotFoundException exception) {
            throw new ServletException("Cannot find session.", exception);
        }

        process(request, response, session);
    }

    private void process(HttpServletRequest request, HttpServletResponse response, Session<I, O, F, L, C> session)
            throws ServletException {
        try {

            Step<O, L> step;
            C dialogueContext = session.getDialogueContext();

            try {
                if (dialogueContext == null) {
                    step = startDialogue(request, response, session);
                } else {
                    step = continueDialogue(request, response, session);
                }
            } catch (Timeout exception) {
                renderError(exception, request, response, session);
                return;
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                renderError(exception, request, response, session);
                return;
            }

            if (step instanceof OutputTurnStep) {
                OutputTurnStep<O, L> outputTurnStep = (OutputTurnStep<O, L>) step;
                renderOutputTurn(outputTurnStep.getOutputTurn(), request, response, session);
            } else if (step instanceof LastTurnStep) {
                LastTurnStep<O, L> lastTurnStep = (LastTurnStep<O, L>) step;
                renderLastTurn(lastTurnStep.getLastTurn(), request, response, session);
            } else if (step instanceof ErrorStep) {
                ErrorStep<O, L> errorStep = (ErrorStep<O, L>) step;
                Throwable throwable = errorStep.getThrowable();
                renderError(throwable, request, response, session);
            }
        } catch (Exception exception) {
            throw new ServletException("Error while rendering step.", exception);
        }
    }

    private Step<O, L> continueDialogue(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Session<I, O, F, L, C> session) throws ServletException, Timeout,
            InterruptedException {
        Assert.notNull(session, "session");
        I inputTurn = createInputTurn(request, response);
        SynchronousDialogueChannel<I, O, F, L, C> dialogueChannel = session.getDialogueChannel();
        Assert.notNull(dialogueChannel, "dialogueChannel");
        return dialogueChannel.doTurn(inputTurn, mDialogueTimeout);
    }

    private Step<O, L> startDialogue(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Session<I, O, F, L, C> session) throws ServletException, Timeout,
            InterruptedException {
        SynchronousDialogueChannel<I, O, F, L, C> dialogueChannel;
        dialogueChannel = new SynchronousDialogueChannel<I, O, F, L, C>();
        session.setDialogueChannel(dialogueChannel);

        Logger logger = mLoggerFactory.getLogger(DIALOGUE_LOGGER_NAME);
        dialogueChannel.setLogger(logger);

        dialogueChannel.setDefaultReceiveFromControllerTimeout(mControllerTimeout);
        dialogueChannel.setDefaultReceiveFromDialogueTimeout(mDialogueTimeout);

        C dialogueContext = createContext(request, session, dialogueChannel, logger);

        DialogueInitializationInfo<I, O, C> initializationInfo;
        initializationInfo = createInitializationInfo(request, response, dialogueContext);
        Dialogue<I, O, F, L, C> dialogue;
        try {
            dialogue = mDialogueFactory.create(initializationInfo);
        } catch (DialogueFactoryException exception) {
            throw new ServletException("Unable to create dialogue.", exception);
        }
        F firstTurn = createFirstTurn(request, response);
        return dialogueChannel.start(dialogue, firstTurn, mDialogueTimeout, dialogueContext);
    }

    private C createContext(HttpServletRequest request,
                            Session<I, O, F, L, C> session,
                            SynchronousDialogueChannel<I, O, F, L, C> dialogueChannel,
                            Logger logger) {
        C dialogueContext = mDialogueContextFactory.createDialogueContext(request,
                                                                          session.getId(),
                                                                          dialogueChannel,
                                                                          logger);
        session.setDialogueContext(dialogueContext);
        return dialogueContext;
    }

    private WebDialogueInitializationInfo<I, O, C> createInitializationInfo(HttpServletRequest request,
                                                                            HttpServletResponse response,
                                                                            C dialogueContext) {
        return new WebDialogueInitializationInfo<I, O, C>(dialogueContext, request, response, getServletContext(), this);
    }

    protected Session<I, O, F, L, C> getSession(HttpServletRequest request) throws SessionNotFoundException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }

            int firstSlash = pathInfo.indexOf('/');
            if (firstSlash != -1) {
                pathInfo = pathInfo.substring(0, firstSlash);
            }

            return getExistingSession(pathInfo);
        } else {
            String sessionId = UUID.randomUUID().toString();

            Session<I, O, F, L, C> session = new Session<I, O, F, L, C>(mSessionContainer, sessionId);
            mSessionContainer.addSession(session);
            if (mWebappServerSessionTrackingEnabled) {
                session.setAssociatedHttpSession(request.getSession());
            }

            return session;
        }
    }

    protected Session<I, O, F, L, C> getExistingSession(String sessionId) throws SessionNotFoundException {
        Session<I, O, F, L, C> session = mSessionContainer.getSession(sessionId);

        if (session == null) throw new SessionNotFoundException("Unable to find session [" + sessionId + "]");

        return session;
    }

    private I createInputTurn(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            return mInputTurnFactory.createInputTurn(request, response);
        } catch (InputTurnFactoryException exception) {
            throw new ServletException(exception);
        }
    }

    private F createFirstTurn(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            return mInputTurnFactory.createFirstTurn(request, response);
        } catch (InputTurnFactoryException exception) {
            throw new ServletException(exception);
        }
    }

    private static void commitToResponse(final HttpServletResponse response, ServletResponseContent responseContent)
            throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType(responseContent.getContentType());
        responseContent.writeTo(outputStream);
    }
}