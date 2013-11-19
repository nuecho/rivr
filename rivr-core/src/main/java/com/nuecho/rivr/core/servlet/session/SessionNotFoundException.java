/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet.session;

/**
 * Thrown when a session could not be found in the SessionContainer. Typically,
 * this occurs when an error occurs on the client-side while the dialogue has
 * terminated on the server-side.
 * 
 * @author Nu Echo Inc.
 */
public final class SessionNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public SessionNotFoundException() {}

    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(Throwable cause) {
        super(cause);
    }
}