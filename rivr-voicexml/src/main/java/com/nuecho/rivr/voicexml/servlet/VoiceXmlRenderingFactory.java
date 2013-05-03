/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import javax.servlet.http.*;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * @author Nu Echo Inc.
 */
public final class VoiceXmlRenderingFactory implements
        DialogueContextFactory<VoiceXmlDialogueContext, VoiceXmlInputTurn, VoiceXmlOutputTurn> {

    @Override
    public VoiceXmlDialogueContext createDialogueContext(HttpServletRequest request,
                                                         String dialogueId,
                                                         DialogueChannel<VoiceXmlInputTurn, VoiceXmlOutputTurn> dialogueChannel,
                                                         Logger logger) {
        return new VoiceXmlDialogueContext(dialogueChannel,
                                           logger,
                                           dialogueId,
                                           request.getContextPath(),
                                           request.getServletPath());
    }
}