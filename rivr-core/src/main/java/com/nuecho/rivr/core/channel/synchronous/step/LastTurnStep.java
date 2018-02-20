/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel.synchronous.step;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;

/**
 * Step wrapping a {@link LastTurn}. This kind of step tells that the dialogue
 * is done.
 *
 * @param <O> type of {@link OutputTurn}
 * @param <L> type of {@link LastTurn}
 * @author Nu Echo Inc.
 */
public final class LastTurnStep<O extends OutputTurn, L extends LastTurn> implements Step<O, L> {

    private final L mLastTurn;

    public LastTurnStep(L lastTurn) {
        Assert.notNull(lastTurn, "lastTurn");
        mLastTurn = lastTurn;
    }

    public L getLastTurn() {
        return mLastTurn;
    }

    @Override
    public String toString() {
        return "Last Turn: " + mLastTurn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mLastTurn == null) ? 0 : mLastTurn.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LastTurnStep other = (LastTurnStep) obj;
        if (mLastTurn == null) {
            if (other.mLastTurn != null) return false;
        } else if (!mLastTurn.equals(other.mLastTurn)) return false;
        return true;
    }
}