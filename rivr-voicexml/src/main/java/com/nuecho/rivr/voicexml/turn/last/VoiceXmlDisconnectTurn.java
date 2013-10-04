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
public class VoiceXmlDisconnectTurn extends VoiceXmlLastTurn {

    private static final String VARIABLES_PROPERTY = "variables";

    private VariableDeclarationList mVariables;

    public VoiceXmlDisconnectTurn(String name) {
        super(name);
    }

    public VoiceXmlDisconnectTurn(String name, VariableDeclarationList variables) {
        super(name);
        Assert.notNull(variables, VARIABLES_PROPERTY);
        mVariables = variables;
    }

    public VariableDeclarationList getVariables() {
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
