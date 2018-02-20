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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        result = prime * result + ((mTime == null) ? 0 : mTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MarkInfo other = (MarkInfo) obj;
        if (mName == null) {
            if (other.mName != null) return false;
        } else if (!mName.equals(other.mName)) return false;
        if (mTime == null) {
            if (other.mTime != null) return false;
        } else if (!mTime.equals(other.mTime)) return false;
        return true;
    }

}