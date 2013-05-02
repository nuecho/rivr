/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;

/**
 * @author Nu Echo Inc.
 */
public interface DialogueContext<I extends InputTurn, O extends OutputTurn> {

    DialogueChannel<I, O> getDialogueChannel();

    Logger getLogger();

    String getDialogueId();
}