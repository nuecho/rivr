/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.last;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class Exit extends VoiceXmlLastTurn {

    private static final String VARIABLES_PROPERTY = "variables";
    private static final String EXPRESSION_PROPERTY = "expression";

    private VariableList mVariables;
    private String mExpression;

    public Exit(String name) {
        super(name);
    }

    public Exit(String name, VariableList variables) {
        super(name);
        Assert.notNull(variables, VARIABLES_PROPERTY);
        mVariables = variables;
    }

    public Exit(String name, String expression) {
        super(name);
        mExpression = expression;
    }

    public VariableList getVariables() {
        return mVariables;
    }

    public String getExpression() {
        return mExpression;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
        JsonUtils.add(builder, EXPRESSION_PROPERTY, mExpression);
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
        Element exitElement = document.createElement(EXIT_ELEMENT);
        if (mVariables != null) {
            addNamelist(blockElement, exitElement, mVariables);
        } else if (mExpression != null) {
            exitElement.setAttribute(EXPR_ATTRIBUTE, mExpression);
        }

        blockElement.appendChild(exitElement);
    }
}
