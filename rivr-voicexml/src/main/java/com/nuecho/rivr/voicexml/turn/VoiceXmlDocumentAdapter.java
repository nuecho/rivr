/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn;

import org.w3c.dom.*;

import com.nuecho.rivr.voicexml.rendering.voicexml.*;

/**
 * @author Nu Echo Inc.
 */
public interface VoiceXmlDocumentAdapter {
    void adaptVoiceXmlDocument(Document voiceXmlDocument) throws VoiceXmlDocumentRenderingException;
}
