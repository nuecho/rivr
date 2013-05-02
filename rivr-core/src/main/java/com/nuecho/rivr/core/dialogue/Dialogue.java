/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import com.nuecho.rivr.core.channel.*;

/**
 * @author Nu Echo Inc.
 */
public interface Dialogue<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>> {

    L run(F firstTurn, C context) throws Exception;

}
