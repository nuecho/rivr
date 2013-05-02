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
 * @author Nu Echo Inc.
 */
/**
 * @author Nu Echo Inc.
 */
public class VoiceXmlDialogueServlet
        extends
        DialogueServlet<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> {

    private static final long serialVersionUID = 1L;

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

    private List<VoiceXmlDocumentAdapter> mVoiceXmlDocumentAdapters;

    protected void initVoiceXmlDialogueServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            if (pathInfo.startsWith(ROOT_PATH)) {
                processRootDocument(request, response, pathInfo);
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
        setDialogueContextFactory(new VoiceXmlRenderingFactory());
        setErrorHandler(new DefaultErrorHandler());
        setParameters();
        initVoiceXmlDialogueServlet();

        mVoiceXmlStepRenderer = new VoiceXmlStepRenderer(mVoiceXmlDocumentAdapters);
        mJsonStepRenderer = new JsonStepRenderer(mVoiceXmlStepRenderer);
    }

    public void setVoiceXmlDocumentAdapters(List<VoiceXmlDocumentAdapter> voiceXmlDocumentAdapters) {
        mVoiceXmlDocumentAdapters = voiceXmlDocumentAdapters;
    }

    private void setParameters() throws DialogueServletInitializationException {

        ILoggerFactory loggerFactory = find("loggerFactory", ILoggerFactory.class);
        if (loggerFactory != null) {
            setLoggerFactory(loggerFactory);
        }

        VoiceXmlDialogueFactory dialogueFactory = find("dialogueFactory", VoiceXmlDialogueFactory.class);
        if (dialogueFactory != null) {
            setDialogueFactory(dialogueFactory);
        }

        VoiceXmlErrorHandler errorHandler = find("errorHandler", VoiceXmlErrorHandler.class);
        if (errorHandler != null) {
            setErrorHandler(errorHandler);
        }

        TimeValue sessionScanPeriod = getTimeValue("sessionScanPeriod");
        if (sessionScanPeriod != null) {
            setSessionScanPeriod(sessionScanPeriod);
        }

        TimeValue sessionTimeout = getTimeValue("sessionTimeout");
        if (sessionTimeout != null) {
            setSessionTimeout(sessionTimeout);
        }

        TimeValue dialogueTimeout = getTimeValue("dialogueTimeout");
        if (dialogueTimeout != null) {
            setDialogueTimeout(dialogueTimeout);
        }
    }

    private TimeValue getTimeValue(String key) throws DialogueServletInitializationException {
        ServletConfig servletConfig = getServletConfig();
        String timeValue = servletConfig.getInitParameter(key);
        if (timeValue == null) return null;
        try {
            return TimeValue.parse(timeValue);
        } catch (IllegalArgumentException exception) {
            throw new DialogueServletInitializationException("Unable to parse time value.", exception);
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
        return find("dialogFactory", VoiceXmlDialogueFactory.class);
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
        @SuppressWarnings("unchecked")
        T castedObject = (T) object;
        return castedObject;
    }

    private <T> T instantiate(String className, Class<T> type, String item)
            throws DialogueServletInitializationException {
        try {
            Class<? extends T> classObject = (Class<? extends T>) Class.forName(className);
            if (!type.isAssignableFrom(classObject))
                throw new DialogueServletInitializationException("Incompatible class type.");
            return classObject.newInstance();
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
        InputStream inputStream = getClass().getResourceAsStream(pathInfo.substring(1));
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String eTag = getETag(bytes);
        String ifNoneMatch = request.getHeader(IF_NONE_MATCH);

        if (eTag.equals(ifNoneMatch)) {
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Not Modifed");
        } else {
            response.setContentType(JAVASCRIPT_CONTENT_TYPE);
            response.addHeader(ETAG, eTag);
            response.getOutputStream().write(bytes);
        }
    }

    private void processRootDocument(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {

        String sessionId = pathInfo.substring(ROOT_PATH.length());

        try {
            Session<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> session = getExistingSession(sessionId);
            Document rootDocument = mRootDocumentFactory.getDocument(request, session);
            response.setContentType(VOICE_XML_CONTENT_TYPE);
            DomUtils.writeToOutputStream(rootDocument, response.getOutputStream());
        } catch (SessionNotFoundException exception) {
            throw new ServletException("Unable to find session.", exception);
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