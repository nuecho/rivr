/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class ClientSideRecording extends AudioItem {

    private static final String EXPRESSION = "expression";
    private static final String CLIENT_SIDE_RECORDING_ELEMENT_TYPE = "clientSideRecording";

    private final String mExpression;

    public ClientSideRecording(String expression) {
        Assert.notNull(expression, "expression");
        mExpression = expression;
    }

    @Override
    public String getElementType() {
        return CLIENT_SIDE_RECORDING_ELEMENT_TYPE;
    }

    public String getExpression() {
        return mExpression;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, EXPRESSION, mExpression);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mExpression == null ? 0 : mExpression.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ClientSideRecording other = (ClientSideRecording) obj;
        if (mExpression == null) {
            if (other.mExpression != null) return false;
        } else if (!mExpression.equals(other.mExpression)) return false;
        return true;
    }

}