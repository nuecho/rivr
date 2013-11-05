/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
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
 * A {@link Script} is a {@link VoiceXmlOutputTurn} that declares
 * variables and/or executes a script.
 * 
 * @author Nu Echo Inc.
 */
public class Script extends VoiceXmlOutputTurn {
    private static final String SCRIPT_TURN_TYPE = "script";

    private static final String SCRIPT_PROPERTY = "script";
    private static final String VARIABLES_PROPERTY = "variables";

    private VariableList mVariables = new VariableList();
    private String mScript;

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
     * @param script The script to execute. <code>null</code> to use the VoiceXML platform default.
     */
    public final void setScript(String script) {
        mScript = script;
    }

    public final VariableList getVariables() {
        return mVariables;
    }

    public final String getScript() {
        return mScript;
    }

    @Override
    protected final String getOuputTurnType() {
        return SCRIPT_TURN_TYPE;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, SCRIPT_PROPERTY, mScript);
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        addVariables(formElement, mVariables);

        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);

        if (mScript != null) {
            Element scriptElement = DomUtils.appendNewElement(blockElement, SCRIPT_ELEMENT);
            DomUtils.appendNewText(scriptElement, mScript);
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
}