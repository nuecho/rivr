/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.voicexml.errorhandling;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;

/**
 * @author Nu Echo Inc.
 */
class AbstractFatalErrorFactory implements FatalErrorFormFactory {

    private final String mElementName;

    public AbstractFatalErrorFactory(String elementName) {
        mElementName = elementName;
    }

    @Override
    public void addFatalErrorForm(Element form, VoiceXmlDocumentTurn turn) {
        Element blockElement = VoiceXmlDomUtil.addBlockElement(form);
        Element element = DomUtils.appendNewElement(blockElement, mElementName);
        customizeForm(form, blockElement, element);
    }

    /**
     * @param form
     * @param blockElement
     * @param element
     */
    protected void customizeForm(Element form, Element blockElement, Element element) {}

}
