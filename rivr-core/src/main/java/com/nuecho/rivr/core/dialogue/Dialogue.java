/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.dialogue;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.servlet.*;

/**
 * Rivr applications are expected to provide one or many implementations of this
 * interface or its subtypes. This interface provides a single method performing
 * the dialogue, i.e. sequence of turn exchanges with the
 * {@link DialogueChannel} and dialogue-related logic.
 * 
 * @param <F> type of {@link FirstTurn}
 * @param <L> type of {@link LastTurn}
 * @param <O> type of {@link OutputTurn}
 * @param <I> type of {@link InputTurn}
 * @param <C> type of {@link DialogueContext}
 * @author Nu Echo Inc.
 */
public interface Dialogue<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>> {

    /**
     * Method called by the controller (e.g. {@link DialogueServlet}) to run the
     * dialogue. It takes initial parameters from the <code>firstTurn</code>
     * parameter and must return a {@link LastTurn}. The <code>context</code>
     * allows the dialogue to access the {@link DialogueChannel} on which turn
     * exchanges can be performed (i.e. exchanges of {@link OutputTurn
     * OutputTurns} and {@link InputTurn InputTurns}).
     * 
     * @param firstTurn First turn. Contains dialogue initialization
     *            information. Cannot be <code>null</code>.
     * @param context Dialogue context. This context gives access to
     *            {@link DialogueChannel}, a {@link Logger} and the dialogue ID.
     *            Cannot be <code>null</code>.
     * @return the result of the dialogue as a {@link LastTurn}. Cannot be
     *         <code>null</code>.
     */
    L run(F firstTurn, C context) throws Exception;

}
