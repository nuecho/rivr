/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import com.nuecho.rivr.core.channel.*;

/**
 * @author Nu Echo Inc.
 */
public interface DialogueChannelListener<I extends InputTurn, O extends OutputTurn> {
    void onStop(DialogueChannel<I, O> dialogueChannel);

    void onStart(DialogueChannel<I, O> dialogueChannel);
}