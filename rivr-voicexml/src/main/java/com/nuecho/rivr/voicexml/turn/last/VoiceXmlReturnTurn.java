/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.last;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class VoiceXmlReturnTurn extends VoiceXmlLastTurn {

    private static final String VARIABLES_PROPERTY = "variables";
    private static final String EVENT_MESSAGE_PROPERTY = "eventMessage";
    private static final String EVENT_NAME_PROPERTY = "eventName";

    private VariableDeclarationList mVariables;
    private String mEventName;
    private String mEventMessage;

    public VoiceXmlReturnTurn(String name, VariableDeclarationList variables) {
        super(name);
        Assert.notNull(variables, VARIABLES_PROPERTY);
        mVariables = variables;
    }

    public VoiceXmlReturnTurn(String name, String eventName, String eventMessage) {
        super(name);
        Assert.notNull(eventName, EVENT_NAME_PROPERTY);
        mEventName = eventName;
        mEventMessage = eventMessage;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
        JsonUtils.add(builder, EVENT_NAME_PROPERTY, mEventName);
        JsonUtils.add(builder, EVENT_MESSAGE_PROPERTY, mEventMessage);
    }

    @Override
    protected Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext, null);
        Element formElement = createForm(document);
        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
        Element returnElement = document.createElement(RETURN_ELEMENT);

        if (mVariables != null) {
            addNamelist(blockElement, returnElement, mVariables);
        } else if (mEventName != null) {
            returnElement.setAttribute(EVENT_ATTRIBUTE, mEventName);

            if (mEventMessage != null) {
                returnElement.setAttribute(MESSAGE_ATTRIBUTE, mEventMessage);
            }
        }
        blockElement.appendChild(returnElement);

        return document;
    }
}
