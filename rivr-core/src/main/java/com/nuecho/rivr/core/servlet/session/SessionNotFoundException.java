/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet.session;

/**
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