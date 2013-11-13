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
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Terminates the {@link VoiceXmlDialogue} with a disconnect (throwing a
 * connection.disconnect.hangup event). A list of variables can optionally be
 * returned.
 * 
 * @author Nu Echo Inc.
 * @see VoiceXmlDialogue
 * @see LastTurn
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml5.3.11">http://www.w3.org/TR/voicexml20/#dml5.3.11</a>
 * @see <a
 *      href="http://www.w3.org/TR/voicexml21/#sec-disconnect">http://www.w3.org/TR/voicexml21/#sec-disconnect</a>
 */
public class Disconnect extends VoiceXmlLastTurn {

    private static final String VARIABLES_PROPERTY = "variables";

    private VariableList mVariables;

    public Disconnect(String name) {
        super(name);
    }

    public Disconnect(String name, VariableList variables) {
        super(name);
        Assert.notNull(variables, VARIABLES_PROPERTY);
        mVariables = variables;
    }

    public VariableList getVariables() {
        return mVariables;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
        Element disconnectElement = document.createElement(DISCONNECT_ELEMENT);

        if (mVariables != null) {
            addNamelist(blockElement, disconnectElement, mVariables);
        }

        blockElement.appendChild(disconnectElement);
    }
}
