/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import com.nuecho.rivr.core.channel.*;

/**
 * Converts a dialogue fatal error (a {@link Throwable} into a {@link LastTurn}
 * 
 * @param <L> type of {@link LastTurn}
 * @author Nu Echo Inc.
 */
public interface ErrorHandler<L extends LastTurn> {
    L handleError(Throwable error);
}
