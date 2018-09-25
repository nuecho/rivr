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
 * Terminates a {@link VoiceXmlDialogue} with an exit, returning the control to
 * the platform. Additional information may be returned with an expression of a
 * list of variables.
 * 
 * @author Nu Echo Inc.
 * @see VoiceXmlDialogue
 * @see LastTurn
 * @see <a
 *      href="https://www.w3.org/TR/voicexml20/#dml5.3.9">https://www.w3.org/TR/voicexml20/#dml5.3.9</a>
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((mExpression == null) ? 0 : mExpression.hashCode());
        result = prime * result + ((mVariables == null) ? 0 : mVariables.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Exit other = (Exit) obj;
        if (mExpression == null) {
            if (other.mExpression != null) return false;
        } else if (!mExpression.equals(other.mExpression)) return false;
        if (mVariables == null) {
            if (other.mVariables != null) return false;
        } else if (!mVariables.equals(other.mVariables)) return false;
        return true;
    }
}
