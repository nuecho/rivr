/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util.logging;

import static org.slf4j.helpers.MessageFormatter.*;

import org.slf4j.*;

/**
 * @author Nu Echo Inc.
 */
public class PrefixedLogger extends LoggerAdapter {

    private final String mPrefix;
    private final Logger mLogger;

    public PrefixedLogger(String prefix, Logger logger) {
        mPrefix = prefix;
        mLogger = logger;
    }

    @Override
    public String getName() {
        return mLogger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return mLogger.isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return mLogger.isTraceEnabled(marker);
    }

    @Override
    public boolean isDebugEnabled() {
        return mLogger.isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return mLogger.isDebugEnabled(marker);
    }

    @Override
    public boolean isInfoEnabled() {
        return mLogger.isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return mLogger.isInfoEnabled(marker);
    }

    @Override
    public boolean isWarnEnabled() {
        return mLogger.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return mLogger.isWarnEnabled(marker);
    }

    @Override
    public boolean isErrorEnabled() {
        return mLogger.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return mLogger.isErrorEnabled(marker);
    }

    @Override
    protected void log(Marker marker, LogLevel logLevel, String message, Throwable throwable) {
        LogUtil.dispatch(mLogger, marker, mPrefix + message, logLevel, throwable);
    }

    @Override
    protected void logWithFormatting(Marker marker, LogLevel logLevel, String format, Object... arguments) {
        LogUtil.dispatch(mLogger, marker, mPrefix + arrayFormat(format, arguments).getMessage(), logLevel, null);
    }
}
