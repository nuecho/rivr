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
     * Performs turn exchange with default timeout.
     *
     * @param <I> input turn type.
     * @param <O> output turn type.
     * @param outputTurn the output turn to return to the controller (i.e. the
     *            servlet).
     * @param context the dialogue context.
     * @return the input turn provided by the controller.
     * @throws Timeout if controller did not provided the input turn within the
     *             allocated time (default timeout)
     * @throws InterruptedException if the dialogue was interrupted.
     */
    public static <I extends InputTurn, O extends OutputTurn> I doTurn(O outputTurn, DialogueContext<I, O> context)
            throws Timeout, InterruptedException {
        return context.getDialogueChannel().doTurn(outputTurn, null);
    }

    /**
     * Performs turn exchange with specified timeout.
     *
     * @param <I> input turn type.
     * @param <O> output turn type.
     * @param outputTurn the output turn to return to the controller (i.e. the
     *            servlet).
     * @param context the dialogue context.
     * @param timeout maximum delay for the controller to provide the input
     *            turn.
     * @return the input turn provided by the controller.
     * @throws Timeout if controller did not provided the input turn within the
     *             allocated time (as provided by the <code>timeout</code>
     *             parameter)
     * @throws InterruptedException if the dialogue was interrupted.
     */
    public static <I extends InputTurn, O extends OutputTurn> I doTurn(O outputTurn,
                                                                       DialogueContext<I, O> context,
                                                                       Duration timeout) throws Timeout,
            InterruptedException {
        return context.getDialogueChannel().doTurn(outputTurn, timeout);
    }

}
