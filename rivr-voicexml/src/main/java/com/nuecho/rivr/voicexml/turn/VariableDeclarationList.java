/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class VariableDeclarationList implements Iterable<VariableDeclaration>, JsonSerializable {

    private static final String VARIABLE_NAME_PROPERTY = "name";
    private static final String INITIAL_VALUE_PROPERTY = "initialValue";

    private final LinkedHashMap<String, VariableDeclaration> mVariables = new LinkedHashMap<String, VariableDeclaration>();

    public void addVariable(VariableDeclaration variableDeclaration) {
        Assert.notNull(variableDeclaration, "variableDeclaration");
        String name = variableDeclaration.getName();
        if (mVariables.containsKey(name)) throw new IllegalArgumentException("Variable " + name + " already present.");

        mVariables.put(name, variableDeclaration);
    }

    public void removeVariable(VariableDeclaration variableDeclaration) {
        Assert.notNull(variableDeclaration, "variableDeclaration");
        mVariables.remove(variableDeclaration.getName());
    }

    public boolean has(String variableName) {
        return mVariables.containsKey(variableName);
    }

    public VariableDeclaration get(String variableName) {
        return mVariables.get(variableName);
    }

    public boolean isEmpty() {
        return mVariables.isEmpty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mVariables == null) ? 0 : mVariables.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VariableDeclarationList other = (VariableDeclarationList) obj;
        if (mVariables == null) {
            if (other.mVariables != null) return false;
        } else if (!mVariables.equals(other.mVariables)) return false;
        return true;
    }

    public static VariableDeclarationList create(JsonObject jsonObject) {
        VariableDeclarationList list = new VariableDeclarationList();
        for (String propertyName : jsonObject.keySet()) {
            JsonValue jsonValue = jsonObject.get(propertyName);

            String ecmaScriptLiteral;
            if (jsonValue instanceof JsonString) {
                JsonString jsonString = (JsonString) jsonValue;
                ecmaScriptLiteral = VoiceXmlDomUtil.createEcmaScriptStringLiteral(jsonString.getString());
            } else {
                ecmaScriptLiteral = jsonValue.toString();
            }

            list.addVariable(new VariableDeclaration(propertyName, ecmaScriptLiteral));
        }
        return list;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonArrayBuilder builder = JsonUtils.createArrayBuilder();
        for (VariableDeclaration variableDeclaration : this) {
            JsonObjectBuilder variableBuilder = JsonUtils.createObjectBuilder();
            JsonUtils.add(variableBuilder, VARIABLE_NAME_PROPERTY, variableDeclaration.getName());
            if (variableDeclaration.getInitialValueExpression() != null) {
                JsonUtils.add(variableBuilder, INITIAL_VALUE_PROPERTY, variableDeclaration.getInitialValueExpression());
            }

            builder.add(variableBuilder);
        }
        return builder.build();
    }

    @Override
    public Iterator<VariableDeclaration> iterator() {
        return Collections.unmodifiableCollection(mVariables.values()).iterator();
    }
}