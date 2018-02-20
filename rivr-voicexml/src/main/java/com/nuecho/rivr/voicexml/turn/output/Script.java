/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import java.util.Map.Entry;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A {@link Script} is a {@link VoiceXmlOutputTurn} that declares variables
 * and/or executes a script.
 * 
 * @author Nu Echo Inc.
 */
public class Script extends VoiceXmlOutputTurn {
    private static final String SCRIPT_TURN_TYPE = "script";

    private static final String CODE_PROPERTY = "code";
    private static final String VARIABLES_PROPERTY = "variables";

    private VariableList mVariables = new VariableList();
    private String mCode;

    /**
     * @param name The name of this turn. Not empty.
     */
    public Script(String name) {
        super(name);
    }

    /**
     * @param variables The list of variables to declare. Not null.
     */
    public final void setVariables(VariableList variables) {
        Assert.notNull(variables, "variables");
        mVariables = variables;
    }

    /**
     * @param code The (optional) code to execute.
     */
    public final void setCode(String code) {
        mCode = code;
    }

    public final VariableList getVariables() {
        return mVariables;
    }

    public final String getCode() {
        return mCode;
    }

    @Override
    protected final String getOuputTurnType() {
        return SCRIPT_TURN_TYPE;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, CODE_PROPERTY, mCode);
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        addVariables(formElement, mVariables);

        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);

        if (mCode != null) {
            Element scriptElement = DomUtils.appendNewElement(blockElement, SCRIPT_ELEMENT);
            DomUtils.appendNewText(scriptElement, mCode);
        }

        StringBuffer scriptBuffer = new StringBuffer();

        scriptBuffer.append(RIVR_SCOPE_OBJECT + ".addValueResult({");
        boolean first = true;
        for (Entry<String, String> entry : mVariables) {
            if (!first) {
                scriptBuffer.append(", ");
            } else {
                first = false;
            }
            scriptBuffer.append("\"");
            scriptBuffer.append(entry.getKey());
            scriptBuffer.append("\": ");
            scriptBuffer.append("dialog.");
            scriptBuffer.append(entry.getKey());
        }
        scriptBuffer.append("});");

        createScript(blockElement, scriptBuffer.toString());
        createGotoSubmit(blockElement);
    }

    /**
     * Builder used to ease the creation of instances of {@link Script}.
     */
    public static class Builder {

        private final String mName;
        private final VariableList mVariables = new VariableList();
        private String mCode;

        public Builder(String name) {
            mName = name;
        }

        public Builder addVariable(String name) {
            mVariables.add(name);
            return this;
        }

        public Builder addVariableString(String name, String string) {
            mVariables.addWithString(name, string);
            return this;
        }

        public Builder addVariableExpression(String name, String expression) {
            mVariables.addWithExpression(name, expression);
            return this;
        }

        public Builder setCode(String code) {
            mCode = code;
            return this;
        }

        public Script build() {
            Script script = new Script(mName);
            script.setCode(mCode);
            script.setVariables(mVariables);
            return script;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((mCode == null) ? 0 : mCode.hashCode());
        result = prime * result + ((mVariables == null) ? 0 : mVariables.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Script other = (Script) obj;
        if (mCode == null) {
            if (other.mCode != null) return false;
        } else if (!mCode.equals(other.mCode)) return false;
        if (mVariables == null) {
            if (other.mVariables != null) return false;
        } else if (!mVariables.equals(other.mVariables)) return false;
        return true;
    }

}