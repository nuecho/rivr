/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel.synchronous.step;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;

/**
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

}