/*
 * Copyright (c) 2002-2005 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.object;

import javax.json.*;

import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author NuEcho Inc.
 */
public enum ParameterValueType implements JsonSerializable {
    DATA("data"), REF("ref");

    private final String mKey;

    private ParameterValueType(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    @Override
    public String toString() {
        return mKey;
    }

    @Override
    public JsonValue asJson() {
        return JsonUtils.wrap(mKey);
    }
}