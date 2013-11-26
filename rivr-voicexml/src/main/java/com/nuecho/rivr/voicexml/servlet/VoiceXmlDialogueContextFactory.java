/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import javax.servlet.http.*;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * Default {@link DialogueContextFactory} for VoiceXML.
 * 
 * @author Nu Echo Inc.
 */
public final class VoiceXmlDialogueContextFactory implements
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