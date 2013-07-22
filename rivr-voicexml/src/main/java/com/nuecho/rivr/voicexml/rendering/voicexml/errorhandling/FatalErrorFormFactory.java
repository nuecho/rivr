/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.rendering.voicexml.errorhandling;

import org.w3c.dom.*;

import com.nuecho.rivr.voicexml.turn.*;

/**
 * @author Nu Echo Inc.
 */
public interface FatalErrorFormFactory {
    void addFatalErrorForm(Element form, VoiceXmlDocumentTurn turn);
}
