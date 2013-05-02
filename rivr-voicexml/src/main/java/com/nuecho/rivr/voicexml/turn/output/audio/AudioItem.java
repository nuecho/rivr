/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class AudioItem implements JsonSerializable {

    public static final String TYPE_PROPERTY = "type";

    public abstract String getElementType();

    protected abstract void addJsonProperties(JsonObjectBuilder builder);

    @Override
    public final String toString() {
        return asJson().toString();
    }

    @Override
    public final JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, TYPE_PROPERTY, getElementType());
        addJsonProperties(builder);
        return builder.build();
    }

}
