/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel;

import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public interface DialogueChannel<I extends InputTurn, O extends OutputTurn> {

    I doTurn(O outputTurn, Duration timeout) throws Timeout, InterruptedException;

    void addListener(DialogueChannelListener<I, O> listener);

    void removeListener(DialogueChannelListener<I, O> listener);
}