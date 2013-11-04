/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * This abstract class is the superclass of all classes representing a turn
 * interpreted by the VoiceXML platform.
 * 
 * @author Nu Echo Inc.
 * @see Interaction
 * @see Message
 * @see ObjectCall
 * @see Script
 * @see SubdialogueCall
 * @see Transfer
 */
public abstract class VoiceXmlOutputTurn extends VoiceXmlDocumentTurn implements OutputTurn {
    private static final String OUTPUT_TURN_TYPE_PROPERTY = "outputTurnType";

    /**
     * @param name The name of this turn. Not empty.
     */
    public VoiceXmlOutputTurn(String name) {
        super(name);
    }

    protected abstract String getOuputTurnType();

    @Override
    protected void addTopLevelProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, OUTPUT_TURN_TYPE_PROPERTY, getOuputTurnType());
    }

    @Override
    protected Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext);
        Element formElement = createForm(document);
        fillVoiceXmlDocument(document, formElement, dialogueContext);
        addEventHandler(document.getDocumentElement());
        addFatalErrorHandlerForm(dialogueContext, document, this);
        addSubmitForm(dialogueContext, document, this);
        return document;
    }

    private static void addEventHandler(Element vxmlElement) {
        Element catchElement = DomUtils.appendNewElement(vxmlElement, CATCH_ELEMENT);

        Element ifErrorElement = DomUtils.appendNewElement(catchElement, IF_ELEMENT);
        ifErrorElement.setAttribute(COND_ATTRIBUTE, "_event.substring(0, 5) == \"error\"");

        Element ifErrorHandlingElement = DomUtils.appendNewElement(ifErrorElement, IF_ELEMENT);
        ifErrorHandlingElement.setAttribute(COND_ATTRIBUTE, RIVR_SCOPE_OBJECT + "." + LOCAL_ERROR_HANDLING_PROPERTY);
        createGotoFatalHandler(ifErrorHandlingElement);

        DomUtils.appendNewElement(ifErrorHandlingElement, ELSE_ELEMENT);

        StringBuilder setErrorHandlingScript = new StringBuilder();
        setErrorHandlingScript.append(RIVR_SCOPE_OBJECT)
                              .append(".")
                              .append(LOCAL_ERROR_HANDLING_PROPERTY)
                              .append("=")
                              .append(TRUE);
        createScript(ifErrorHandlingElement, setErrorHandlingScript.toString());

        StringBuilder addEventScript = new StringBuilder();
        addEventScript.append(RIVR_SCOPE_OBJECT)
                      .append(".addEventResult(")
                      .append(EVENT_NAME_VARIABLE)
                      .append(", ")
                      .append(EVENT_MESSAGE_VARIABLE)
                      .append(")");

        createScript(catchElement, addEventScript.toString());
        createGotoSubmit(catchElement);
    }

    protected abstract void fillVoiceXmlDocument(Document document,
                                                 Element formElement,
                                                 VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException;
}