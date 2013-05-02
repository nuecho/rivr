/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import javax.servlet.http.*;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.dialogue.*;

/**
 * @author Nu Echo Inc.
 */
public interface DialogueContextFactory<C extends DialogueContext<I, O>, I extends InputTurn, O extends OutputTurn> {
    C createDialogueContext(HttpServletRequest request,
                            String dialogueId,
                            DialogueChannel<I, O> dialogueChannel,
                            Logger logger);
}