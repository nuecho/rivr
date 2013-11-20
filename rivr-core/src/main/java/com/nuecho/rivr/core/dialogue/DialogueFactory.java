/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import com.nuecho.rivr.core.channel.*;

/**
 * @author Nu Echo Inc.
 */
public interface DialogueFactory<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>> {
    Dialogue<I, O, F, L, C> create(DialogueInitializationInfo<I, O, C> dialogueInitializationInfo)
            throws DialogueFactoryException;
}
