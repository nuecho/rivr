/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel.synchronous.step;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;

/**
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

}