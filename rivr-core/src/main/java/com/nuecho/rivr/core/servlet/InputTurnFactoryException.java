/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

/**
 * @author Nu Echo Inc.
 */
public final class InputTurnFactoryException extends Exception {

    private static final long serialVersionUID = 1L;

    public InputTurnFactoryException() {}

    public InputTurnFactoryException(String message) {
        super(message);
    }

    public InputTurnFactoryException(Throwable cause) {
        super(cause);
    }

    public InputTurnFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}