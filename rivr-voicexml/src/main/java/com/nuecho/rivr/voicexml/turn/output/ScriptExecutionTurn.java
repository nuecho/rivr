/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>ScriptExecutionTurn</code> is a <code>VoiceXmlOutputTurn</code> that
 * declares variables and/or executes a script.
 * 
 * @author Nu Echo Inc.
 */
public final class ScriptExecutionTurn extends VoiceXmlOutputTurn {
    private static final String SCRIPT_EXECUTION_TYPE = "scriptExecution";
    private static final String SCRIPT_PROPERTY = "script";
    private static final String VARIABLES_PROPERTY = "variables";

    private VariableDeclarationList mVariables = new VariableDeclarationList();
    private String mScript;

    /**
     * @param name The name of this turn. Not empty.
     */
    public ScriptExecutionTurn(String name) {
        super(name);
    }

    /**
     * @param variables The list of variables to declare. Not null.
     */
    public void setVariables(VariableDeclarationList variables) {
        Assert.notNull(variables, "variables");
        mVariables = variables;
    }

    /**
     * @param script The script to execute. Null resets the value.
     */
    public void setScript(String script) {
        mScript = script;
    }

    public VariableDeclarationList getVariables() {
        return mVariables;
    }

    public String getScript() {
        return mScript;
    }

    @Override
    protected String getOuputTurnType() {
        return SCRIPT_EXECUTION_TYPE;
    }

    @Override
    protected JsonValue getTurnAsJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, SCRIPT_PROPERTY, mScript);
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
        return builder.build();
    }

    @Override
    public Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext, null);
        Element formElement = createForm(document);

        addVariableDeclarations(formElement, mVariables);

        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);

        if (mScript != null) {
            Element scriptElement = DomUtils.appendNewElement(blockElement, SCRIPT_ELEMENT);
            DomUtils.appendNewText(scriptElement, mScript);
        }

        StringBuffer scriptBuffer = new StringBuffer();

        scriptBuffer.append(RIVR_SCOPE_OBJECT + ".addValueResult({");
        boolean first = true;
        for (VariableDeclaration variableDeclaration : mVariables) {
            if (!first) {
                scriptBuffer.append(", ");
            } else {
                first = false;
            }
            scriptBuffer.append("\"");
            scriptBuffer.append(variableDeclaration.getName());
            scriptBuffer.append("\": ");
            scriptBuffer.append("dialog.");
            scriptBuffer.append(variableDeclaration.getName());
        }
        scriptBuffer.append("});");

        createScript(blockElement, scriptBuffer.toString());
        createGotoSubmit(blockElement);

        addSubmitForm(dialogueContext, document, this);

        return document;
    }
}