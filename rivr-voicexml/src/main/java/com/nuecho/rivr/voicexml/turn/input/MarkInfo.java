/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.input;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * {@link MarkInfo} contains the name of the last mark encountered before a
 * recognition and the time elapsed between the beginning of the interaction and
 * the start of the recognition.
 * 
 * @author Nu Echo Inc.
 */
public final class MarkInfo implements JsonSerializable {

    private static final String TIME_PROPERTY = "time";
    private static final String NAME_PROPERTY = "name";
    private final String mName;
    private final Duration mTime;

    public MarkInfo(String name, Duration time) {
        Assert.notEmpty(name, NAME_PROPERTY);
        mName = name;
        mTime = time;
    }

    public String getName() {
        return mName;
    }

    public Duration getTime() {
        return mTime;
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, NAME_PROPERTY, mName);
        JsonUtils.addDurationProperty(builder, TIME_PROPERTY, mTime);
        return builder.build();
    }
}