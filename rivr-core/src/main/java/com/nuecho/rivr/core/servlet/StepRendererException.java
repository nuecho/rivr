package com.nuecho.rivr.core.servlet;

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