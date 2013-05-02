/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import javax.servlet.http.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.servlet.session.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.first.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * @author Nu Echo Inc.
 */
public interface VoiceXmlRootDocumentFactory {

    Document getDocument(HttpServletRequest request,
                         Session<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> session)
            throws VoiceXmlDocumentRenderingException;

}
