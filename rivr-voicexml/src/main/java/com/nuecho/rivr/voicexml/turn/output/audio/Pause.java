/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>Pause</code> represents a silence of a specified duration in an
 * {@link AudioItem} sequence.
 * 
 * @author Nu Echo Inc.
 */
public final class Pause extends AudioItem {
    private static final String PAUSE_PROPERTY = "pause";
    public static final String PAUSE_ELEMENT_TYPE = PAUSE_PROPERTY;

    private final TimeValue mDuration;

    /**
     * @param duration The duration of the pause. Not null.
     */
    public Pause(TimeValue duration) {
        mDuration = duration;
    }

    @Override
    public String getElementType() {
        return PAUSE_ELEMENT_TYPE;
    }

    public TimeValue getDuration() {
        return mDuration;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.addTimeProperty(builder, PAUSE_PROPERTY, mDuration);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mDuration == null) ? 0 : mDuration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Pause other = (Pause) obj;
        if (mDuration == null) {
            if (other.mDuration != null) return false;
        } else if (!mDuration.equals(other.mDuration)) return false;
        return true;
    }
}