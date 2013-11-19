/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;

/**
 * Utility class for dialogue-related operations.
 * 
 * @author Nu Echo Inc.
 */
public final class DialogueUtils {

    private DialogueUtils() {
        //utility class: instantiation forbidden
    }

    /**
     * Perform turn exchange with default timeout.
     */
    public static <I extends InputTurn, O extends OutputTurn> I doTurn(O outputTurn, DialogueContext<I, O> context)
            throws Timeout, InterruptedException {
        return context.getDialogueChannel().doTurn(outputTurn, null);
    }

    /**
     * Perform turn exchange with specified timeout.
     */
    public static <I extends InputTurn, O extends OutputTurn> I doTurn(O outputTurn,
                                                                       DialogueContext<I, O> context,
                                                                       Duration timeout) throws Timeout,
            InterruptedException {
        return context.getDialogueChannel().doTurn(outputTurn, timeout);
    }

}
