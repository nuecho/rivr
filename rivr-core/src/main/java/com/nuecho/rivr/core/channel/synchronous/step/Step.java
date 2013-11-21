/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel.synchronous.step;

import com.nuecho.rivr.core.channel.*;

/**
 * A {@link Step} is the result coming from the dialogue following a turn
 * exchange.
 * <p>
 * Step can be one of the following:
 * <ul>
 * <li>{@link OutputTurnStep}</li>
 * <li>{@link LastTurnStep}</li>
 * <li>{@link ErrorStep}</li>
 * </ul>
 * 
 * @param <O> type of {@link OutputTurn}
 * @param <L> type of {@link LastTurn}
 * @author Nu Echo Inc.
 */
public interface Step<O extends OutputTurn, L extends LastTurn> {}
