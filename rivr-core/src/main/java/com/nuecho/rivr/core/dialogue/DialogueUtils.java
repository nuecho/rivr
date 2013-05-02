/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class DialogueUtils {

    private DialogueUtils() {
        //utility class: instantiation forbidden
    }

    public static <I extends InputTurn, O extends OutputTurn> I doTurn(DialogueContext<I, O> context, O outputTurn)
            throws Timeout, InterruptedException {
        return context.getDialogueChannel().doTurn(outputTurn, null);
    }

    public static <I extends InputTurn, O extends OutputTurn> I doTurn(DialogueContext<I, O> context,
                                                                       O outputTurn,
                                                                       TimeValue timeout) throws Timeout,
            InterruptedException {
        return context.getDialogueChannel().doTurn(outputTurn, timeout);
    }

}
