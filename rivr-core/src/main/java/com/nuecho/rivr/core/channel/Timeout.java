/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel;

/**
 * Exception thrown when {@link DialogueChannel} exceeds maximum time allowed to
 * perform an operation.
 * 
 * @author Nu Echo Inc.
 */
public final class Timeout extends Exception {

    private static final long serialVersionUID = 1L;

    public Timeout() {}

    public Timeout(String message, Throwable cause) {
        super(message, cause);
    }

    public Timeout(String message) {
        super(message);
    }

    public Timeout(Throwable cause) {
        super(cause);
    }
}