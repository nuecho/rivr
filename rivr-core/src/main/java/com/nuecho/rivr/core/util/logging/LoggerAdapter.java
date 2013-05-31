/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util.logging;

import static com.nuecho.rivr.core.util.logging.LogLevel.*;

import org.slf4j.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class LoggerAdapter implements Logger {
    /**
     * To be overridden.
     */
    protected void log(Marker marker, LogLevel logLevel, String message, Throwable throwable) {}

    /**
     * To be overridden.
     */
    protected void logWithFormatting(Marker marker, LogLevel logLevel, String format, Object... arguments) {}

    @Override
    public void trace(String message) {
        log(null, TRACE, message, null);
    }

    @Override
    public void trace(String format, Object argument) {
        logWithFormatting(null, TRACE, format, argument);
    }

    @Override
    public void trace(String format, Object argument1, Object argument2) {
        logWithFormatting(null, TRACE, format, argument1, argument2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logWithFormatting(null, TRACE, format, arguments);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        log(null, TRACE, message, throwable);
    }

    @Override
    public void trace(Marker marker, String message) {
        log(marker, TRACE, message, null);
    }

    @Override
    public void trace(Marker marker, String format, Object argument) {
        logWithFormatting(marker, TRACE, format, argument);
    }

    @Override
    public void trace(Marker marker, String format, Object argument1, Object argument2) {
        logWithFormatting(marker, TRACE, format, argument1, argument2);
    }

    @Override
    public void trace(Marker marker, String format, Object... arguments) {
        logWithFormatting(marker, TRACE, format, arguments);
    }

    @Override
    public void trace(Marker marker, String message, Throwable throwable) {
        log(marker, TRACE, message, throwable);
    }

    @Override
    public void debug(String message) {
        log(null, DEBUG, message, null);
    }

    @Override
    public void debug(String format, Object argument) {
        logWithFormatting(null, DEBUG, format, argument);
    }

    @Override
    public void debug(String format, Object argument1, Object argument2) {
        logWithFormatting(null, DEBUG, format, argument1, argument2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logWithFormatting(null, DEBUG, format, arguments);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        log(null, DEBUG, message, throwable);
    }

    @Override
    public void debug(Marker marker, String message) {
        log(marker, DEBUG, message, null);
    }

    @Override
    public void debug(Marker marker, String format, Object argument) {
        logWithFormatting(marker, DEBUG, format, argument);
    }

    @Override
    public void debug(Marker marker, String format, Object argument1, Object argument2) {
        logWithFormatting(marker, DEBUG, format, argument1, argument2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        logWithFormatting(marker, DEBUG, format, arguments);
    }

    @Override
    public void debug(Marker marker, String message, Throwable throwable) {
        log(marker, DEBUG, message, throwable);
    }

    @Override
    public void info(String message) {
        log(null, INFO, message, null);
    }

    @Override
    public void info(String format, Object argument) {
        logWithFormatting(null, INFO, format, argument);
    }

    @Override
    public void info(String format, Object argument1, Object argument2) {
        logWithFormatting(null, INFO, format, argument1, argument2);
    }

    @Override
    public void info(String format, Object... arguments) {
        logWithFormatting(null, INFO, format, arguments);
    }

    @Override
    public void info(String message, Throwable throwable) {
        log(null, INFO, message, throwable);
    }

    @Override
    public void info(Marker marker, String message) {
        log(marker, INFO, message, null);
    }

    @Override
    public void info(Marker marker, String format, Object argument) {
        logWithFormatting(marker, INFO, format, argument);
    }

    @Override
    public void info(Marker marker, String format, Object argument1, Object argument2) {
        logWithFormatting(marker, INFO, format, argument1, argument2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        logWithFormatting(marker, INFO, format, arguments);
    }

    @Override
    public void info(Marker marker, String message, Throwable throwable) {
        log(marker, INFO, message, throwable);
    }

    @Override
    public void warn(String message) {
        log(null, WARN, message, null);
    }

    @Override
    public void warn(String format, Object argument) {
        logWithFormatting(null, WARN, format, argument);
    }

    @Override
    public void warn(String format, Object argument1, Object argument2) {
        logWithFormatting(null, WARN, format, argument1, argument2);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logWithFormatting(null, WARN, format, arguments);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        log(null, WARN, message, throwable);
    }

    @Override
    public void warn(Marker marker, String message) {
        log(marker, WARN, message, null);
    }

    @Override
    public void warn(Marker marker, String format, Object argument) {
        logWithFormatting(marker, WARN, format, argument);
    }

    @Override
    public void warn(Marker marker, String format, Object argument1, Object argument2) {
        logWithFormatting(marker, WARN, format, argument1, argument2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        logWithFormatting(marker, WARN, format, arguments);
    }

    @Override
    public void warn(Marker marker, String message, Throwable throwable) {
        log(marker, WARN, message, throwable);
    }

    @Override
    public void error(String message) {
        log(null, ERROR, message, null);
    }

    @Override
    public void error(String format, Object argument) {
        logWithFormatting(null, ERROR, format, argument);
    }

    @Override
    public void error(String format, Object argument1, Object argument2) {
        logWithFormatting(null, ERROR, format, argument1, argument2);
    }

    @Override
    public void error(String format, Object... arguments) {
        logWithFormatting(null, ERROR, format, arguments);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log(null, ERROR, message, throwable);
    }

    @Override
    public void error(Marker marker, String message) {
        log(marker, ERROR, message, null);
    }

    @Override
    public void error(Marker marker, String format, Object argument) {
        logWithFormatting(marker, ERROR, format, argument);
    }

    @Override
    public void error(Marker marker, String format, Object argument1, Object argument2) {
        logWithFormatting(marker, ERROR, format, argument1, argument2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        logWithFormatting(marker, ERROR, format, arguments);
    }

    @Override
    public void error(Marker marker, String message, Throwable throwable) {
        log(marker, ERROR, message, throwable);
    }
}
