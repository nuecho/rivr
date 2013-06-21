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

    @Override
    protected JsonValue getTurnAsJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
        return builder.build();
    }

    @Override
    protected Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext, this);
        Element formElement = createForm(document);
        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
        Element disconnectElement = document.createElement(DISCONNECT_ELEMENT);

        if (mVariables != null) {
            addNamelist(blockElement, disconnectElement, mVariables);
        }

        blockElement.appendChild(disconnectElement);
        return document;
    }
}
