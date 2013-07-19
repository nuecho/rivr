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
public class VoiceXmlExitTurn extends VoiceXmlLastTurn {

    private static final String VARIABLES_PROPERTY = "variables";
    private static final String EXPRESSION_PROPERTY = "expression";

    private VariableDeclarationList mVariables;
    private String mExpression;

    public VoiceXmlExitTurn(String name) {
        super(name);
    }

    public VoiceXmlExitTurn(String name, VariableDeclarationList variables) {
        super(name);
        Assert.notNull(variables, VARIABLES_PROPERTY);
        mVariables = variables;
    }

    public VoiceXmlExitTurn(String name, String expression) {
        super(name);
        mExpression = expression;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
        JsonUtils.add(builder, EXPRESSION_PROPERTY, mExpression);
    }

    @Override
    protected Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext, this);
        Element formElement = createForm(document);
        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
        Element exitElement = document.createElement(EXIT_ELEMENT);
        if (mVariables != null) {
            addNamelist(blockElement, exitElement, mVariables);
        } else if (mExpression != null) {
            exitElement.setAttribute(EXPR_ATTRIBUTE, mExpression);
        }
        blockElement.appendChild(exitElement);

        return document;
    }
}
