/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel;

import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;

/**
 * Core concept of Rivr, a {@link DialogueChannel} is the interface between the
 * {@link Dialogue} and the controller (such as the {@link DialogueServlet}).
 * <p>
 * The dialogue produces {@link OutputTurn OutputTurns} and sends them to the
 * controller via the {@link #doTurn(OutputTurn, Duration)} method. The
 * DialogueChannel will return the response from the controller as an
 * {@link InputTurn}.
 * <p>
 * Note: The {@link DialogueChannel} is typically wrapped in
 * {@link DialogueContext}.
 * 
 * @param <O> type of {@link OutputTurn}
 * @param <I> type of {@link InputTurn}
 * @author Nu Echo Inc.
 */
public interface DialogueChannel<I extends InputTurn, O extends OutputTurn> {

    /**
     * Performs a turn exchange: the dialogue channel will return the
     * {@link InputTurn}
     * 
     * @param outputTurn The output turn to send. Cannot be <code>null</code>.
     * @param timeout maximum time allowed to receive the turn from the
     *            dialogue. If <code>null</code>, uses the default value of this
     *            dialogue channel. A value of Duration.ZERO (or equivalent)
     *            means to wait forever.
     * @return the received {@link InputTurn}. Cannot be <code>null</code>.
     * @throws Timeout if the dialogue channel has not been able to give the
     *             InputTurn before <code>timeout</code> parameter.
     * @throws InterruptedException if the thread has been interrupted while
     *             waiting for the result.
     */
    I doTurn(O outputTurn, Duration timeout) throws Timeout, InterruptedException;

    /**
     * Adds a listener to this dialogue channel.
     */
    void addListener(DialogueChannelListener<I, O> listener);

    /**
     * Removes a listener from this dialogue channel.
     */
    void removeListener(DialogueChannelListener<I, O> listener);
}