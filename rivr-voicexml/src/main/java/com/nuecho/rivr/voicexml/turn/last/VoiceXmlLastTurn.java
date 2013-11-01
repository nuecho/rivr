/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.last;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class VoiceXmlLastTurn extends VoiceXmlDocumentTurn implements LastTurn {

    public VoiceXmlLastTurn(String name) {
        super(name);
    }

    @Override
    protected Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext);
        Element formElement = createForm(document);
        fillVoiceXmlDocument(document, formElement, dialogueContext);
        addEventHandlers(document.getDocumentElement());
        addFatalErrorHandlerForm(dialogueContext, document, this);
        return document;
    }

    private void addEventHandlers(Element vxmlElement) {
        Element catchElement = DomUtils.appendNewElement(vxmlElement, CATCH_ELEMENT);
        catchElement.setAttribute(EVENT_ATTRIBUTE, ERROR_EVENT_NAME);
        createGotoFatalHandler(catchElement);
    }

    protected abstract void fillVoiceXmlDocument(Document document,
                                                 Element formElement,
                                                 VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException;

}
