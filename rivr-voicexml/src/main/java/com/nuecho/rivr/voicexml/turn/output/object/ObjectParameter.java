/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.object;

import javax.json.*;

import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class ObjectParameter implements JsonSerializable {

    private static final String NAME_PROPERTY = "name";
    private static final String VALUE_PROPERTY = "value";
    private static final String TYPE_PROPERTY = "type";
    private static final String VALUE_TYPE_PROPERTY = "valueType";
    private static final String EXPRESSION_PROPERTY = "expression";

    private final String mName;
    private String mExpression;
    private String mValue;
    private ParameterValueType mValueType;
    private String mType;

    private ObjectParameter(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public String getExpression() {
        return mExpression;
    }

    public String getValue() {
        return mValue;
    }

    public static ObjectParameter createWithValue(String name, String value) {
        ObjectParameter objectParameter = new ObjectParameter(name);
        objectParameter.mValue = value;
        return objectParameter;
    }

    public static ObjectParameter createWithExpression(String name, String expression) {
        ObjectParameter objectParameter = new ObjectParameter(name);
        objectParameter.mExpression = expression;
        return objectParameter;
    }

    public static ObjectParameter createWithJson(String name, JsonValue json) {
        return createWithExpression(name, json.toString());
    }

    public ParameterValueType getValueType() {
        return mValueType;
    }

    public void setValueType(ParameterValueType valueType) {
        mValueType = valueType;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, NAME_PROPERTY, mName);
        JsonUtils.add(builder, EXPRESSION_PROPERTY, mExpression);
        JsonUtils.add(builder, VALUE_PROPERTY, mValue);
        JsonUtils.add(builder, TYPE_PROPERTY, mType);
        JsonUtils.add(builder, VALUE_TYPE_PROPERTY, mValueType);
        return builder.build();
    }
}
