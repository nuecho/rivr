/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

/**
 * @author Nu Echo Inc.
 */
public final class DialogueServletInitializationException extends Exception {

    private static final long serialVersionUID = 1L;

    public DialogueServletInitializationException() {
        super();
    }

    public DialogueServletInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DialogueServletInitializationException(String message) {
        super(message);
    }

    public DialogueServletInitializationException(Throwable cause) {
        super(cause);
    }
}