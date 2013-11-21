/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import javax.servlet.http.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.dialogue.*;

/**
 * Renders {@link OutputTurn} and {@link LastTurn} into
 * {@link ServletResponseContent}
 * 
 * @author Nu Echo Inc.
 */
public interface StepRenderer<I extends InputTurn, O extends OutputTurn, L extends LastTurn, C extends DialogueContext<I, O>> {

    ServletResponseContent createDocumentForOutputTurn(O outputTurn,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       C dialogueContext) throws StepRendererException;

    ServletResponseContent createDocumentForLastTurn(L lastTurn,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     C dialogueContext) throws StepRendererException;

}