/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.core.util.logging;

import java.util.*;

import org.slf4j.*;
import org.slf4j.helpers.*;

/**
 * @author Nu Echo Inc.
 */
public final class AccumulatingLog extends LoggerAdapter {

    private final List<LogEntry> mMessages = new ArrayList<LogEntry>();
    private final String mName;

    public AccumulatingLog(String name) {
        mName = name;
    }

    public void flushTo(Logger log) {
        for (LogEntry entry : mMessages) {
            entry.dispatch(log);
        }

        clear();
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return true;
    }

    @Override
    protected void log(Marker marker, LogLevel logLevel, String message, Throwable throwable) {
        mMessages.add(new LogEntry(logLevel, message, throwable, marker));
    }

    @Override
    protected void logWithFormatting(Marker marker, LogLevel logLevel, String format, Object... argumentArray) {
        mMessages.add(new LogEntry(logLevel, MessageFormatter.format(format, argumentArray), null, marker));
    }

    public void clear() {
        mMessages.clear();
    }

    public List<LogEntry> getMessages() {
        return Collections.unmodifiableList(mMessages);
    }

    public static class LogEntry {

        private final String mMessage;
        private final Throwable mThrowable;
        private final LogLevel mLogLevel;
        private final Marker mMarker;

        public LogEntry(LogLevel logLevel, String message, Throwable throwable, Marker marker) {
            mLogLevel = logLevel;
            mMessage = message;
            mThrowable = throwable;
            mMarker = marker;
        }

        public String getMessage() {
            return mMessage;
        }

        public Throwable getThrowable() {
            return mThrowable;
        }

        public LogLevel getLogLevel() {
            return mLogLevel;
        }

        public Marker getMarker() {
            return mMarker;
        }

        public void dispatch(Logger log) {
            LogUtil.dispatch(log, mMarker, mMessage, mLogLevel, mThrowable);
        }
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        for (LogEntry element : mMessages) {
            out.append(element.getMessage());
            out.append("\n");
        }

        return out.toString();
    }
}