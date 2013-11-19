/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
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

}