/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import javax.servlet.*;
import javax.servlet.http.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.dialogue.*;

/**
 * Servlet-specific {@link DialogueInitializationInfo}.
 * 
 * @author Nu Echo Inc.
 */
public class WebDialogueInitializationInfo<I extends InputTurn, O extends OutputTurn, C extends DialogueContext<I, O>>
        implements DialogueInitializationInfo<I, O, C> {

    private final C mDialogueContext;

    private final HttpServletRequest mHttpServletRequest;
    private final HttpServletResponse mHttpServletResponse;
    private final ServletContext mServletContext;
    private final HttpServlet mServlet;

    public WebDialogueInitializationInfo(C dialogueContext,
                                         HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         ServletContext servletContext,
                                         HttpServlet servlet) {
        mDialogueContext = dialogueContext;
        mHttpServletRequest = httpServletRequest;
        mHttpServletResponse = httpServletResponse;
        mServletContext = servletContext;
        mServlet = servlet;
    }

    @Override
    public C getContext() {
        return mDialogueContext;
    }

    public HttpServletRequest getHttpServletRequest() {
        return mHttpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return mHttpServletResponse;
    }

    public ServletContext getServletContext() {
        return mServletContext;
    }

    public HttpServlet getServlet() {
        return mServlet;
    }

}
