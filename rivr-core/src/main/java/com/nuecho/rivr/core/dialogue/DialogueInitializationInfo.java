/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import com.nuecho.rivr.core.channel.*;

/**
 * @author Nu Echo Inc.
 */
public interface DialogueInitializationInfo<I extends InputTurn, O extends OutputTurn, C extends DialogueContext<I, O>> {

    C getContext();

}