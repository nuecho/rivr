/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.subdialogue;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class SubdialogueParameter implements JsonSerializable {
    private static final String NAME_PROPERTY = "name";
    private static final String VALUE_PROPERTY = "value";
    private static final String EXPRESSION_PROPERTY = "expression";

    private final String mName;
    private String mExpression;
    private String mValue;

    /**
     * @param name The name of the parameter. Not empty.
     * @param value The string value of the parameter. Not null.
     * @return The newly created subdialogue parameter
     */
    public static SubdialogueParameter createWithValue(String name, String value) {
        Assert.notNull(value, "value");

        SubdialogueParameter parameter = new SubdialogueParameter(name);
        parameter.mValue = value;
        return parameter;
    }

    /**
     * @param name The name of the parameter. Not empty.
     * @param json The JSON value of the parameter. Not null.
     * @return The newly created subdialogue parameter
     */
    public static SubdialogueParameter createWithJson(String name, JsonValue json) {
        Assert.notNull(json, "json");
        return createWithExpression(name, json.toString());
    }

    /**
     * @param name The name of the parameter. Not empty.
     * @param expression The ECMAScript expression of the parameter. Not null.
     * @return The newly created subdialogue parameter
     */
    public static SubdialogueParameter createWithExpression(String name, String expression) {
        Assert.notNull(expression, "expression");

        SubdialogueParameter parameter = new SubdialogueParameter(name);
        parameter.mExpression = expression;
        return parameter;
    }

    private SubdialogueParameter(String name) {
        Assert.notEmpty(name, "name");
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

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, NAME_PROPERTY, mName);
        JsonUtils.add(builder, EXPRESSION_PROPERTY, mExpression);
        JsonUtils.add(builder, VALUE_PROPERTY, mValue);
        return builder.build();
    }
}