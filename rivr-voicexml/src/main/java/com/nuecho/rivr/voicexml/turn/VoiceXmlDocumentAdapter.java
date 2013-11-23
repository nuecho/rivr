/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn;

import org.w3c.dom.*;

import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * Specifies a transformation on a VoiceXML document in its {@link Document
 * org.w3c.dom.Document} form. Custom implementations can be set on
 * {@link VoiceXmlOutputTurn} and {@link VoiceXmlLastTurn} in order to modify
 * the generated VoiceXML for a specific VoiceXML platform (platform extension,
 * apply work-around for non-compliance towards VoiceXML 2.1 specification,
 * etc).
 * 
 * @author Nu Echo Inc.
 */
public interface VoiceXmlDocumentAdapter {
    void adaptVoiceXmlDocument(Document voiceXmlDocument) throws VoiceXmlDocumentRenderingException;
}
