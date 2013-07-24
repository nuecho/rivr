/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.object;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
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

    /**
     * @param name The name of the parameter. Not empty.
     * @param value The string value of the parameter. Not null.
     * @return The newly created object parameter
     */
    public static ObjectParameter createWithValue(String name, String value) {
        Assert.notEmpty(name, "name");
        Assert.notNull(value, "value");

        ObjectParameter objectParameter = new ObjectParameter(name);
        objectParameter.mValue = value;
        return objectParameter;
    }

    /**
     * @param name The name of the parameter. Not empty.
     * @param expression The ECMAScript expression of the parameter. Not null.
     * @return The newly created object parameter
     */
    public static ObjectParameter createWithExpression(String name, String expression) {
        Assert.notEmpty(name, "name");
        Assert.notNull(expression, "expression");

        ObjectParameter objectParameter = new ObjectParameter(name);
        objectParameter.mExpression = expression;
        return objectParameter;
    }

    /**
     * @param name The name of the parameter. Not empty.
     * @param json The JSON value of the parameter. Not null.
     * @return The newly created object parameter
     */
    public static ObjectParameter createWithJson(String name, JsonValue json) {
        Assert.notEmpty(name, "name");
        Assert.notNull(json, "json");

        return createWithExpression(name, json.toString());
    }

    private ObjectParameter(String name) {
        mName = name;
    }

    /**
     * @param valueType One of {@link ParameterValueType#DATA} or
     *            {@link ParameterValueType#REF}. Indicates to an object if the
     *            value associated with name is data or a URI. Null reverts to
     *            VoiceXML default value.
     */
    public void setValueType(ParameterValueType valueType) {
        mValueType = valueType;
    }

    /**
     * @param type The media type of the result provided by a URI if the value
     *            type is {@link ParameterValueType#REF}. Null reverts to
     *            VoiceXML default value.
     */
    public void setType(String type) {
        mType = type;
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

    public ParameterValueType getValueType() {
        return mValueType;
    }

    public String getType() {
        return mType;
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