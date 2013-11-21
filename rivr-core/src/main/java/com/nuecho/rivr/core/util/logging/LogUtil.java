/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util.logging;

import org.slf4j.*;

/**
 * Internal logging utilities.
 * 
 * @author Nu Echo Inc.
 */
public class LogUtil {

    public static void dispatch(Logger log, Marker marker, String message, LogLevel logLevel, Throwable throwable) {

        boolean hasThrowable = throwable != null;

        if (marker != null) {
            switch (logLevel) {
                case ALL:
                case TRACE:
                    if (hasThrowable) {
                        log.trace(marker, message, throwable);
                    } else {
                        log.trace(marker, message);
                    }
                    break;
                case DEBUG:
                    if (hasThrowable) {
                        log.debug(marker, message, throwable);
                    } else {
                        log.debug(marker, message);
                    }
                    break;
                case INFO:
                    if (hasThrowable) {
                        log.info(marker, message, throwable);
                    } else {
                        log.info(marker, message);
                    }
                    break;
                case WARN:
                    if (hasThrowable) {
                        log.warn(marker, message, throwable);
                    } else {
                        log.warn(marker, message);
                    }
                    break;
                case ERROR:
                    if (hasThrowable) {
                        log.error(marker, message, throwable);
                    } else {
                        log.error(marker, message);
                    }
                    break;
                case OFF:
            }
        } else {
            switch (logLevel) {
                case ALL:
                case TRACE:
                    if (hasThrowable) {
                        log.trace(message, throwable);
                    } else {
                        log.trace(message);
                    }
                    break;
                case DEBUG:
                    if (hasThrowable) {
                        log.debug(message, throwable);
                    } else {
                        log.debug(message);
                    }
                    break;
                case INFO:
                    if (hasThrowable) {
                        log.info(message, throwable);
                    } else {
                        log.info(message);
                    }
                    break;
                case WARN:
                    if (hasThrowable) {
                        log.warn(message, throwable);
                    } else {
                        log.warn(message);
                    }
                    break;
                case ERROR:
                    if (hasThrowable) {
                        log.error(message, throwable);
                    } else {
                        log.error(message);
                    }
                    break;
                case OFF:
            }
        }
    }
}
