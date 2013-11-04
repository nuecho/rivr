/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
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
 * @author Nu Echo Inc.
 */
public abstract class DialogueServlet<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>>
        extends HttpServlet {

    private static final String MDC_KEY_DIALOGUE_ID = "dialogueId";

    private static final String SESSION_LOGGER_NAME = "com.nuecho.rivr.session";
    private static final String DIALOGUE_LOGGER_NAME = "com.nuecho.rivr.dialogue";

    private static final long serialVersionUID = 1L;
    private static final String SESSION_CONTAINER_NAME = "com.nuecho.rivr.sessionContainer";

    private ErrorHandler<L> mErrorHandler;
    private DialogueFactory<I, O, F, L, C> mDialogueFactory;
    private DialogueContextFactory<C, I, O> mDialogueContextFactory;
    private ILoggerFactory mLoggerFactory;
    private SessionContainer<I, O, F, L, C> mSessionContainer;
    private InputTurnFactory<I, F> mInputTurnFactory;

    private TimeValue mDialogueTimeout = TimeValue.milliseconds(10000);
    private TimeValue mSessionTimeout = TimeValue.minutes(30);
    private TimeValue mSessionScanPeriod = TimeValue.minutes(2);

    protected abstract void initDialogueServlet() throws DialogueServletInitializationException;

    protected abstract void destroyDialogueServlet();

    protected abstract StepRenderer<I, O, L, C> getStepRenderer(HttpServletRequest request,
                                                                Session<I, O, F, L, C> session);

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

    }

    @Override
    public void destroy() {
        destroyDialogueServlet();
    }

    private void ensureFieldIsSet(Object fieldValue, String fieldName) {
        if (fieldValue == null) throw new IllegalStateException(fieldName + "  is not set.");
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

    public void renderOutputTurn(O outputTurn,
                                 HttpServletRequest request,
                                 final HttpServletResponse response,
                                 Session<I, O, F, L, C> session) throws IOException, StepRendererException {
        ServletResponseContent responseContent = getStepRenderer(request, session).createDocumentForOutputTurn(outputTurn,
                                                                                                               request,
                                                                                                               response,
                                                                                                               session.getDialogueContext());
        commitToResponse(response, responseContent);
    }

    public void renderLastTurn(L result,
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

    public void renderError(Throwable error,
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

    public final void setDialogueTimeout(TimeValue dialogueTimeout) {
        Assert.notNull(dialogueTimeout, "dialogueTimeout");
        mDialogueTimeout = dialogueTimeout;
    }

    public void setSessionTimeout(TimeValue sessionTimeout) {
        Assert.notNull(sessionTimeout, "sessionTimeout");
        mSessionTimeout = sessionTimeout;
    }

    public void setSessionScanPeriod(TimeValue sessionScanPeriod) {
        Assert.notNull(sessionScanPeriod, "sessionScanPeriod");
        mSessionScanPeriod = sessionScanPeriod;
    }

    public void setErrorHandler(ErrorHandler<L> errorHandler) {
        Assert.notNull(errorHandler, "errorHandler");
        mErrorHandler = errorHandler;
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