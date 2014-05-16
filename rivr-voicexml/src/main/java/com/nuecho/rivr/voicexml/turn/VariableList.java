/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn;

import java.util.*;
import java.util.Map.Entry;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Collection of pairs of variable names and initial expression. The collection
 * ensure that variable names are valid ECMAScript identifiers. The collection
 * preserves order as it internal container is a {@link LinkedHashMap}.
 * 
 * @author Nu Echo Inc.
 */
public final class VariableList implements Iterable<Entry<String, String>>, JsonSerializable {

    private static final String VARIABLE_NAME_PROPERTY = "name";
    private static final String INITIAL_VALUE_PROPERTY = "initialValue";

    private final LinkedHashMap<String, String> mVariables = new LinkedHashMap<String, String>();

    public void add(String name) {
        addWithExpression(name, null);
    }

    public void addWithExpression(String name, String initialExpression) {
        Assert.notNull(name, "name");
        Assert.ensure(VoiceXmlUtils.isValidIdentifierName(name), "Invalid ECMAScript identifier name: '" + name + "'");
        mVariables.put(name, initialExpression);
    }

    public void addWithString(String name, String string) {
        Assert.notNull(name, "name");
        Assert.ensure(VoiceXmlUtils.isValidIdentifierName(name), "Invalid ECMAScript identifier name: '" + name + "'");
        Assert.notNull(string, "string");
        mVariables.put(name, VoiceXmlDomUtil.createEcmaScriptStringLiteral(string));
    }

    public void remove(String name) {
        Assert.notNull(name, "name");
        mVariables.remove(name);
    }

    public boolean has(String variableName) {
        return mVariables.containsKey(variableName);
    }

    public String get(String variableName) {
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
        VariableList other = (VariableList) obj;
        if (mVariables == null) {
            if (other.mVariables != null) return false;
        } else if (!mVariables.equals(other.mVariables)) return false;
        return true;
    }

    public static VariableList create(JsonObject jsonObject) {
        VariableList list = new VariableList();
        for (String propertyName : jsonObject.keySet()) {
            JsonValue jsonValue = jsonObject.get(propertyName);
            list.addWithExpression(propertyName, jsonValue.toString());
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
        for (Entry<String, String> entry : this) {
            JsonObjectBuilder variableBuilder = JsonUtils.createObjectBuilder();
            JsonUtils.add(variableBuilder, VARIABLE_NAME_PROPERTY, entry.getKey());
            if (entry.getValue() != null) {
                JsonUtils.add(variableBuilder, INITIAL_VALUE_PROPERTY, entry.getValue());
            }

            builder.add(variableBuilder);
        }
        return builder.build();
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return Collections.unmodifiableCollection(mVariables.entrySet()).iterator();
    }
}