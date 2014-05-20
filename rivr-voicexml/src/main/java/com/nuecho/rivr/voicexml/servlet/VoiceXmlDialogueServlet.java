/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import java.io.*;
import java.security.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.*;
import org.w3c.dom.*;

import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.servlet.session.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.json.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.first.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.util.*;

/**
 * Implementation of the {@link DialogueServlet} specialized for VoiceXML. This
 * servlet handles requests from the VoiceXML platform and responds with
 * VoiceXML documents. It also intercepts special resources (
 * <code>/script</code> and <code>/root</code>).
 * <p/>
 * <h3>init args</h3> The following servlet initial arguments are supported:
 * <dt>com.nuecho.rivr.voicexml.errorHandler.class</dt>
 * <dd>Class name of the error handlder. This class must implements
 * {@link VoiceXmlErrorHandler}, be public and non-abstract and have a public
 * no-argument constructor. Default:
 * <code>com.nuecho.rivr.voicexml.servlet.DefaultErrorHandler</code></dd>
 * <p/>
 * <dt>com.nuecho.rivr.voicexml.errorHandler.key</dt>
 * <dd>As an alternative to
 * <code>com.nuecho.rivr.voicexml.errorHandler.class</code>, this indicates the
 * servlet context attribute name under which the {@link VoiceXmlErrorHandler}
 * can be found. Default: (none: an instance of
 * <code>com.nuecho.rivr.voicexml.servlet.DefaultErrorHandler</code> is used)</dd>
 * <p/>
 * <dt>com.nuecho.rivr.voicexml.dialogueFactory.class</dt>
 * <dd>Class name of the dialogue factory. This class must implements
 * {@link VoiceXmlDialogueFactory}, be public and non-abstract and have a public
 * no-argument constructor. Default: (none).</dd>
 * <p/>
 * <dt>com.nuecho.rivr.voicexml.dialogueFactory.key</dt>
 * <dd>As an alternative to
 * <code>com.nuecho.rivr.voicexml.dialogueFactory.class</code>, this indicates
 * the servlet context attribute name under which the
 * {@link VoiceXmlDialogueFactory} can be found. Default: (none)</dd>
 * <p/>
 * <dt>com.nuecho.rivr.voicexml.dialogue.class</dt>
 * <dd>If neither <code>com.nuecho.rivr.voicexml.dialogueFactory.class</code>
 * nor <code>com.nuecho.rivr.voicexml.dialogueFactory.key</code> is specified,
 * this specifies the class name of the dialogue. This class must implements
 * {@link VoiceXmlDialogue}, be public and non-abstract and have a public
 * no-argument constructor. Default: (none).</dd>
 * <p/>
 * <dt>com.nuecho.rivr.voicexml.dialogue.key</dt>
 * <dd>If neither <code>com.nuecho.rivr.voicexml.dialogueFactory.class</code>,
 * <code>com.nuecho.rivr.voicexml.dialogueFactory.key</code> nor
 * <code>com.nuecho.rivr.voicexml.dialogue.class</code> is specified, this
 * specifies the servlet context attribute name under which the
 * {@link VoiceXmlDialogue} can be found. Default: (none)</dd>
 * <p/>
 * <dt>com.nuecho.rivr.voicexml.loggerFactory.class</dt>
 * <dd>Class name of the dialogue factory. This class must implements
 * {@link LoggerFactory org.slf4j.LoggerFactory}, be public and non-abstract and
 * have a public no-argument constructor. Default: (none:
 * {@link org.slf4j.LoggerFactory#getILoggerFactory()} is used as the logger
 * factory).</dd>
 * <p/>
 * <dt>com.nuecho.rivr.voicexml.loggerFactory.key</dt>
 * <dd>As an alternative to
 * <code>com.nuecho.rivr.voicexml.loggerFactory.class</code>, this indicates the
 * servlet context attribute name under which the {@link LoggerFactory
 * org.slf4j.LoggerFactory} can be found. Default: (none:
 * {@link org.slf4j.LoggerFactory#getILoggerFactory()} is used as the logger
 * factory).</dd>
 * <p>
 * <b>Important:</b> one of the following must be specified, they are mutually
 * exclusive:
 * <ul>
 * <li>com.nuecho.rivr.voicexml.dialogueFactory.class</li>
 * <li>com.nuecho.rivr.voicexml.dialogueFactory.key</li>
 * <li>com.nuecho.rivr.voicexml.dialogue.class</li>
 * <li>com.nuecho.rivr.voicexml.dialogue.key</li>
 * </ul>
 * 
 * @author Nu Echo Inc.
 */
public class VoiceXmlDialogueServlet
        extends
        DialogueServlet<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> {

    private static final long serialVersionUID = 1L;

    private static final String INITIAL_ARGUMENT_PREFIX = "com.nuecho.rivr.voicexml.";
    private static final String INITIAL_ARGUMENT_ERROR_HANDLER = INITIAL_ARGUMENT_PREFIX + "errorHandler";
    private static final String INITIAL_ARGUMENT_DIALOGUE_FACTORY = INITIAL_ARGUMENT_PREFIX + "dialogueFactory";
    private static final String INITIAL_ARGUMENT_DIALOGUE = INITIAL_ARGUMENT_PREFIX + "dialogue";
    private static final String INITIAL_ARGUMENT_LOGGER_FACTORY = INITIAL_ARGUMENT_PREFIX + "loggerFactory";

    public static final String ROOT_PATH = "/root/";
    public static final String RIVR_SCRIPT = "/scripts/rivr.js";

    private static final String IF_NONE_MATCH = "If-None-Match";
    private static final String ETAG = "ETag";

    private VoiceXmlStepRenderer mVoiceXmlStepRenderer;
    private JsonStepRenderer mJsonStepRenderer;

    public static final String VOICE_XML_CONTENT_TYPE = "application/voicexml+xml";
    public static final String JAVASCRIPT_CONTENT_TYPE = "application/javascript";

    private static final String ACCEPT_HEADER = "Accept";

    private VoiceXmlRootDocumentFactory mRootDocumentFactory = new DefaultVoiceXmlRootDocumentFactory();

    private List<? extends VoiceXmlDocumentAdapter> mVoiceXmlDocumentAdapters;

    protected void initializeVoiceXmlDialogueServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            if (pathInfo.startsWith(ROOT_PATH)) {
                processRootDocument(request, response);
                return;
            }

            if (RIVR_SCRIPT.equals(pathInfo)) {
                processRessource(request, response, pathInfo);
                return;
            }
        }

        super.doGet(request, response);
    }

    public void setRootDocumentFactory(VoiceXmlRootDocumentFactory rootDocumentFactory) {
        Assert.notNull(rootDocumentFactory, "rootDocumentFactory");
        mRootDocumentFactory = rootDocumentFactory;
    }

    @Override
    protected final void initDialogueServlet() throws DialogueServletInitializationException {
        setInputTurnFactory(new VoiceXmlInputTurnFactory());
        setDialogueContextFactory(new VoiceXmlDialogueContextFactory());
        setErrorHandler(new DefaultErrorHandler());
        initializeProperties();
        initializeVoiceXmlDialogueServlet();

        mVoiceXmlStepRenderer = new VoiceXmlStepRenderer(mVoiceXmlDocumentAdapters);
        mJsonStepRenderer = new JsonStepRenderer(mVoiceXmlStepRenderer);
    }

    @Override
    protected void destroyDialogueServlet() {}

    public void setVoiceXmlDocumentAdapters(List<VoiceXmlDocumentAdapter> voiceXmlDocumentAdapters) {
        mVoiceXmlDocumentAdapters = voiceXmlDocumentAdapters;
    }

    private void initializeProperties() throws DialogueServletInitializationException {

        ILoggerFactory loggerFactory = find(INITIAL_ARGUMENT_LOGGER_FACTORY, ILoggerFactory.class);
        if (loggerFactory != null) {
            setLoggerFactory(loggerFactory);
        }

        VoiceXmlDialogueFactory dialogueFactory = getDialogueFactory();
        if (dialogueFactory != null) {
            setDialogueFactory(dialogueFactory);
        } else {
            setImplicitDialogueFactory();
        }

        VoiceXmlErrorHandler errorHandler = find(INITIAL_ARGUMENT_ERROR_HANDLER, VoiceXmlErrorHandler.class);
        if (errorHandler != null) {
            setErrorHandler(errorHandler);
        }

    }

    private void setImplicitDialogueFactory() throws DialogueServletInitializationException {
        ServletConfig servletConfig = getServletConfig();
        String className = servletConfig.getInitParameter(INITIAL_ARGUMENT_DIALOGUE + ".class");
        String key = servletConfig.getInitParameter(INITIAL_ARGUMENT_DIALOGUE + ".key");

        if (className != null) {
            try {
                Class<?> rawDialogueClass = Class.forName(className);
                if (!VoiceXmlDialogue.class.isAssignableFrom(rawDialogueClass)) {
                    String message = "Dialogue class "
                                     + rawDialogueClass
                                     + " does not implements "
                                     + VoiceXmlDialogue.class.getName()
                                     + ".";
                    throw new DialogueServletInitializationException(message);
                }

                @SuppressWarnings("unchecked")
                Class<? extends VoiceXmlDialogue> dialogueClass = (Class<? extends VoiceXmlDialogue>) rawDialogueClass;
                setDialogueFactory(new SimpleVoiceXmlDialogueFactory(dialogueClass));
            } catch (ClassNotFoundException exception) {
                throw new DialogueServletInitializationException("Cannot find dialogue class.", exception);
            } catch (DialogueFactoryException exception) {
                throw new DialogueServletInitializationException("Cannot initialize dialogue factory.", exception);
            }
        } else if (key != null) {
            VoiceXmlDialogue dialogue = findInServletContext(key, VoiceXmlDialogue.class, key);
            setDialogueFactory(new SimpleVoiceXmlDialogueFactory(dialogue));
        }
    }

    private <T> T find(String prefix, Class<T> type) throws DialogueServletInitializationException {
        T object = null;
        ServletConfig servletConfig = getServletConfig();
        String className = servletConfig.getInitParameter(prefix + ".class");
        if (className != null) {
            object = instantiate(className, type, prefix);
        }

        String key = servletConfig.getInitParameter(prefix + ".key");
        if (key != null) {
            object = findInServletContext(key, type, prefix);
        }
        return object;
    }

    protected VoiceXmlDialogueFactory getDialogueFactory() throws DialogueServletInitializationException {
        return find(INITIAL_ARGUMENT_DIALOGUE_FACTORY, VoiceXmlDialogueFactory.class);
    }

    private <T> T findInServletContext(String servletContextKey, Class<T> type, String item)
            throws DialogueServletInitializationException {
        Object object = getServletContext().getAttribute(servletContextKey);

        if (object == null)
            throw new DialogueServletInitializationException("Cannot find "
                                                             + item
                                                             + " with name ["
                                                             + servletContextKey
                                                             + "] in servlet context.");

        if (!(type.isInstance(object)))
            throw new DialogueServletInitializationException("Servlet context object ["
                                                             + servletContextKey
                                                             + "] does not implements "
                                                             + type.getName()
                                                             + ". Actual class is "
                                                             + object.getClass().getName());
        return type.cast(object);
    }

    private <T> T instantiate(String className, Class<T> type, String item)
            throws DialogueServletInitializationException {
        try {
            Class<?> classObject = Class.forName(className);
            if (!type.isAssignableFrom(classObject))
                throw new DialogueServletInitializationException("Incompatible class type.");
            return type.cast(classObject.newInstance());
        } catch (ClassNotFoundException exception) {
            throw new DialogueServletInitializationException("Cannot find " + item + " class '" + className + "'",
                                                             exception);
        } catch (InstantiationException exception) {
            throw new DialogueServletInitializationException("Cannot instantiate "
                                                             + item
                                                             + " of class '"
                                                             + className
                                                             + "'", exception);
        } catch (IllegalAccessException exception) {
            throw new DialogueServletInitializationException("Cannot access " + item + " class '" + className + "'",
                                                             exception);
        }
    }

    @Override
    protected StepRenderer<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> getStepRenderer(HttpServletRequest request,
                                                                                                                             Session<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> session) {
        String acceptHeader = request.getHeader(ACCEPT_HEADER);
        if (acceptHeader == null) return mVoiceXmlStepRenderer;
        if (acceptHeader.indexOf("application/json") != -1) return mJsonStepRenderer;
        if (acceptHeader.indexOf("application/javascript") != -1) return mJsonStepRenderer;
        if (acceptHeader.indexOf("application/voicexml+xml") != -1) return mVoiceXmlStepRenderer;
        return mVoiceXmlStepRenderer;
    }

    private void processRessource(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws IOException, ServletException {
        InputStream inputStream = VoiceXmlDialogueServlet.class.getResourceAsStream(pathInfo.substring(1));
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String eTag = getETag(bytes);
        String ifNoneMatch = request.getHeader(IF_NONE_MATCH);

        if (eTag.equals(ifNoneMatch)) {
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Not Modified");
        } else {
            response.setContentType(JAVASCRIPT_CONTENT_TYPE);
            response.addHeader(ETAG, eTag);
            response.getOutputStream().write(bytes);
        }
    }

    private void processRootDocument(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        try {
            Document rootDocument = mRootDocumentFactory.getDocument(request);
            response.setContentType(VOICE_XML_CONTENT_TYPE);
            DomUtils.writeToOutputStream(rootDocument, response.getOutputStream(), Encoding.UTF_8);
        } catch (VoiceXmlDocumentRenderingException exception) {
            throw new ServletException("Error while rendering root document.", exception);
        }
    }

    private String getETag(byte[] bytes) throws ServletException {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(bytes);
            return StringUtils.bytesToHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new ServletException("Could not create message digest.", exception);
        }
    }

}