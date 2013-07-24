/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>Mark</code> represents a marker in an {@link AudioItem} sequence
 * which can be used to determine when a user started to speak.
 * 
 * @author Nu Echo Inc.
 * @see MarkInfo
 */
public final class Mark extends AudioItem {
    private static final String MARK_PROPERTY = "mark";
    private static final String MARK_ELEMENT_TYPE = "mark";
    private final String mName;

    /**
     * @param name The name of the mark. Not null.
     */
    public Mark(String name) {
        Assert.notNull(name, "name");
        mName = name;
    }

    @Override
    public String getElementType() {
        return MARK_ELEMENT_TYPE;
    }

    public String getName() {
        return mName;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, MARK_PROPERTY, mName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mName == null ? 0 : mName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Mark other = (Mark) obj;
        if (mName == null) {
            if (other.mName != null) return false;
        } else if (!mName.equals(other.mName)) return false;
        return true;
    }
}