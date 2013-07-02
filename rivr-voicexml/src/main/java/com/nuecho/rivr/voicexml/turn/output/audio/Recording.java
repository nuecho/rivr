/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>Recording</code> represents an audio file with an optional synthesis
 * text backup.
 * 
 * @author Nu Echo Inc.
 * @see SynthesisText
 * @see ResourceFetchConfiguration
 */
public final class Recording extends AudioItem {
    public static final String RECORDING_ELEMENT_TYPE = "recording";
    private static final String RESOURCE_FETCH_CONFIGURATION_PROPERTY = "resourceFetchConfiguration";
    private static final String PATH_PROPERTY = "path";

    private final String mPath;
    private final SynthesisText mSynthesisText;
    private ResourceFetchConfiguration mResourceFetchConfiguration;

    /**
     * @param path The URI of the audio prompt. Not empty.
     */
    public Recording(String path) {
        this(path, null);
    }

    /**
     * @param path The URI of the audio prompt. Not empty.
     * @param synthesisText The synthesis text to play if the audio prompt
     *            cannot be found.
     */
    public Recording(String path, SynthesisText synthesisText) {
        Assert.notEmpty(path, "path");
        mPath = path;
        mSynthesisText = synthesisText;
    }

    /**
     * @param resourceFetchConfiguration The resource fetch configuration. Null
     *            resets the value.
     */
    public void setResourceFetchConfiguration(ResourceFetchConfiguration resourceFetchConfiguration) {
        mResourceFetchConfiguration = resourceFetchConfiguration;
    }

    @Override
    public String getElementType() {
        return RECORDING_ELEMENT_TYPE;
    }

    public String getPath() {
        return mPath;
    }

    public SynthesisText getSynthesisText() {
        return mSynthesisText;
    }

    public ResourceFetchConfiguration getResourceFetchConfiguration() {
        return mResourceFetchConfiguration;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, PATH_PROPERTY, mPath);
        JsonUtils.add(builder, RESOURCE_FETCH_CONFIGURATION_PROPERTY, mResourceFetchConfiguration);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mPath == null ? 0 : mPath.hashCode());
        result = prime * result + (mResourceFetchConfiguration == null ? 0 : mResourceFetchConfiguration.hashCode());
        result = prime * result + (mSynthesisText == null ? 0 : mSynthesisText.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Recording other = (Recording) obj;
        if (mPath == null) {
            if (other.mPath != null) return false;
        } else if (!mPath.equals(other.mPath)) return false;
        if (mResourceFetchConfiguration == null) {
            if (other.mResourceFetchConfiguration != null) return false;
        } else if (!mResourceFetchConfiguration.equals(other.mResourceFetchConfiguration)) return false;
        if (mSynthesisText == null) {
            if (other.mSynthesisText != null) return false;
        } else if (!mSynthesisText.equals(other.mSynthesisText)) return false;
        return true;
    }
}