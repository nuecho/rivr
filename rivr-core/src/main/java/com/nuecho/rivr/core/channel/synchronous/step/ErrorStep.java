/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel.synchronous.step;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class ErrorStep<O extends OutputTurn, L extends LastTurn> implements Step<O, L> {

    private final Throwable mThrowable;

    public ErrorStep(Throwable throwable) {
        Assert.notNull(throwable, "throwable");
        mThrowable = throwable;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    @Override
    public String toString() {
        return "Error: " + mThrowable.toString();
    }
}
