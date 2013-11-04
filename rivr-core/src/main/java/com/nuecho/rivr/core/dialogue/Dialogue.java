/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import com.nuecho.rivr.core.channel.*;

/**
 * @param <I> Type of the input turn.
 * @param <O> Type of the output turn.
 * @param <F> Type of the first turn.
 * @param <L> Type of the last turn.
 * @param <C> Type of the dialogue context.
 * @author Nu Echo Inc.
 */
public interface Dialogue<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>> {

    L run(F firstTurn, C context) throws Exception;

}
