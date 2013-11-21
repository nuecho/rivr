/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import com.nuecho.rivr.core.channel.*;

/**
 * Information passed to the dialogue factory for dialogue creation.
 * 
 * @param <O> type of {@link OutputTurn}
 * @param <I> type of {@link InputTurn}
 * @param <C> type of {@link DialogueContext}
 * @author Nu Echo Inc.
 */
public interface DialogueInitializationInfo<I extends InputTurn, O extends OutputTurn, C extends DialogueContext<I, O>> {

    C getContext();

}