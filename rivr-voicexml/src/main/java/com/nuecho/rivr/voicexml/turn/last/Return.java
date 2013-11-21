/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.last;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Terminates a {@link VoiceXmlDialogue} with a return, transferring the control
 * back to the calling dialogue. A list of variables or an event can be
 * returned. Must be used it the {@link VoiceXmlDialogue} was called using a
 * {@link SubdialogueCall}.
 * 
 * @author Nu Echo Inc.
 * @see VoiceXmlDialogue
 * @see LastTurn
 * @see SubdialogueCall
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml5.3.10">http://www.w3.org/TR/voicexml20/#dml5.3.10</a>
 */
public class Return extends VoiceXmlLastTurn {

    private static final String VARIABLES_PROPERTY = "variables";
    private static final String EVENT_MESSAGE_PROPERTY = "eventMessage";
    private static final String EVENT_NAME_PROPERTY = "eventName";

    private VariableList mVariables;
    private String mEventName;
    private String mEventMessage;

    public Return(String name, VariableList variables) {
        super(name);
        Assert.notNull(variables, VARIABLES_PROPERTY);
        mVariables = variables;
    }

    public Return(String name, String eventName, String eventMessage) {
        super(name);
        Assert.notNull(eventName, EVENT_NAME_PROPERTY);
        mEventName = eventName;
        mEventMessage = eventMessage;
    }

    public VariableList getVariables() {
        return mVariables;
    }

    public String getEventName() {
        return mEventName;
    }

    public String getEventMessage() {
        return mEventMessage;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
        JsonUtils.add(builder, EVENT_NAME_PROPERTY, mEventName);
        JsonUtils.add(builder, EVENT_MESSAGE_PROPERTY, mEventMessage);
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
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
    }
}
