/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import com.nuecho.rivr.core.channel.*;

/**
 * Receives notification of start and stop events on the dialogue associated
 * with a {@link DialogueChannel}.
 * 
 * @author Nu Echo Inc.
 */
public interface DialogueChannelListener<I extends InputTurn, O extends OutputTurn> {
    void onStart(DialogueChannel<I, O> dialogueChannel);

    void onStop(DialogueChannel<I, O> dialogueChannel);
}