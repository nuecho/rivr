/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import javax.servlet.http.*;

import com.nuecho.rivr.core.channel.*;

/**
 * @author Nu Echo Inc.
 */
public interface InputTurnFactory<I extends InputTurn, F extends FirstTurn> {
    I createInputTurn(HttpServletRequest request, HttpServletResponse response) throws InputTurnFactoryException;

    F createFirstTurn(HttpServletRequest request, HttpServletResponse response) throws InputTurnFactoryException;
}