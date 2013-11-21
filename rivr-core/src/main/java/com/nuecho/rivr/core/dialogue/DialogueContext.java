/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;

/**
 * Interface for dialogue context: an access to {@link DialogueChannel},
 * {@link Logger} and dialogue ID. The {@link DialogueContext} is passed to the
 * {@link Dialogue} in the {@link Dialogue#run(FirstTurn, DialogueContext)}
 * method.
 * 
 * @param <O> type of {@link OutputTurn}
 * @param <I> type of {@link InputTurn}
 * @author Nu Echo Inc.
 */
public interface DialogueContext<I extends InputTurn, O extends OutputTurn> {

    DialogueChannel<I, O> getDialogueChannel();

    Logger getLogger();

    String getDialogueId();
}