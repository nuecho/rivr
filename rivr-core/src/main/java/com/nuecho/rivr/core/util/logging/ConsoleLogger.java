/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util.logging;

import java.io.*;

import org.slf4j.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class ConsoleLogger extends LoggerAdapter {

    private final LogLevel mLogLevel;

    public ConsoleLogger(LogLevel logLevel) {
        mLogLevel = logLevel;
    }

    @Override
    public abstract String getName();

    @Override
    protected void log(Marker marker, LogLevel logLevel, String message, Throwable throwable) {
        PrintStream printStream = getPrintStream();
        printStream.println(String.format("[%5s]", logLevel) + " " + message);
        if (throwable != null) {
            throwable.printStackTrace(printStream);
        }
    }

    protected abstract PrintStream getPrintStream();

    @Override
    public boolean isTraceEnabled() {
        return mLogLevel.ordinal() >= LogLevel.TRACE.ordinal();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return mLogLevel.ordinal() >= LogLevel.TRACE.ordinal();
    }

    @Override
    public boolean isDebugEnabled() {
        return mLogLevel.ordinal() >= LogLevel.DEBUG.ordinal();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return mLogLevel.ordinal() >= LogLevel.DEBUG.ordinal();
    }

    @Override
    public boolean isInfoEnabled() {
        return mLogLevel.ordinal() >= LogLevel.INFO.ordinal();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return mLogLevel.ordinal() >= LogLevel.INFO.ordinal();
    }

    @Override
    public boolean isWarnEnabled() {
        return mLogLevel.ordinal() >= LogLevel.WARN.ordinal();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return mLogLevel.ordinal() >= LogLevel.WARN.ordinal();
    }

    @Override
    public boolean isErrorEnabled() {
        return mLogLevel.ordinal() >= LogLevel.ERROR.ordinal();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return mLogLevel.ordinal() >= LogLevel.ERROR.ordinal();
    }

}
