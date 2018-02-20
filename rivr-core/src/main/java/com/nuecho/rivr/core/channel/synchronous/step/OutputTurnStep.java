/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel.synchronous.step;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;

/**
 * Step wrapping an {@link OutputTurn}. This kind of step tells that the
 * dialogue emits an {@link OutputTurn} that should be processed by the
 * controller and that an {@link InputTurn} must be sent back to dialogue
 * afterward.
 *
 * @param <O> type of {@link OutputTurn}
 * @param <L> type of {@link LastTurn}
 * @author Nu Echo Inc.
 */
public final class OutputTurnStep<O extends OutputTurn, L extends LastTurn> implements Step<O, L> {

    private final O mOutputTurn;

    public OutputTurnStep(O outputTurn) {
        Assert.notNull(outputTurn, "outputTurn");
        mOutputTurn = outputTurn;
    }

    public O getOutputTurn() {
        return mOutputTurn;
    }

    @Override
    public String toString() {
        return "Output Turn: " + mOutputTurn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mOutputTurn == null) ? 0 : mOutputTurn.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        OutputTurnStep other = (OutputTurnStep) obj;
        if (mOutputTurn == null) {
            if (other.mOutputTurn != null) return false;
        } else if (!mOutputTurn.equals(other.mOutputTurn)) return false;
        return true;
    }

}