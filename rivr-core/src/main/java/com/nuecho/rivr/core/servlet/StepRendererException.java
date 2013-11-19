package com.nuecho.rivr.core.servlet;

import com.nuecho.rivr.core.channel.*;

/**
 * Error occurring during rendering of {@link OutputTurn} and {@link LastTurn}.
 * 
 * @author Nu Echo Inc.
 */
public final class StepRendererException extends Exception {

    private static final long serialVersionUID = 1L;

    public StepRendererException() {}

    public StepRendererException(String message) {
        super(message);
    }

    public StepRendererException(Throwable cause) {
        super(cause);
    }

    public StepRendererException(String message, Throwable cause) {
        super(message, cause);
    }
}