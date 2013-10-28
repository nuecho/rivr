/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>AudioFile</code> represents an audio file with an optional synthesis
 * text backup.
 * 
 * @author Nu Echo Inc.
 * @see SynthesisText
 * @see ResourceFetchConfiguration
 */
public final class AudioFile extends AudioItem {
    public static final String AUDIO_FILE_ELEMENT_TYPE = "audioFile";
    private static final String RESOURCE_FETCH_CONFIGURATION_PROPERTY = "resourceFetchConfiguration";
    private static final String PATH_PROPERTY = "path";
    private static final String EXPRESSION_PROPERTY = "expression";
    private static final String ALTERNATIVE_PROPERTY = "alternative";

    private String mPath;
    private String mExpression;
    private SynthesisText mAlternative;
    private ResourceFetchConfiguration mResourceFetchConfiguration;

    public static AudioFile fromPath(String path) {
        AudioFile audioFile = new AudioFile();
        audioFile.mPath = path;
        return audioFile;
    }

    public static AudioFile fromExpression(String expression) {
        AudioFile audioFile = new AudioFile();
        audioFile.mExpression = expression;
        return audioFile;
    }

    /**
     * @param resourceFetchConfiguration The resource fetch configuration. Null
     *            reverts to VoiceXML default value.
     */
    public void setResourceFetchConfiguration(ResourceFetchConfiguration resourceFetchConfiguration) {
        mResourceFetchConfiguration = resourceFetchConfiguration;
    }

    public void setAlternative(SynthesisText alternative) {
        mAlternative = alternative;
    }

    @Override
    public String getElementType() {
        return AUDIO_FILE_ELEMENT_TYPE;
    }

    public String getPath() {
        return mPath;
    }

    public String getExpression() {
        return mExpression;
    }

    public SynthesisText getAlternative() {
        return mAlternative;
    }

    public ResourceFetchConfiguration getResourceFetchConfiguration() {
        return mResourceFetchConfiguration;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, PATH_PROPERTY, mPath);
        JsonUtils.add(builder, EXPRESSION_PROPERTY, mExpression);
        JsonUtils.add(builder, RESOURCE_FETCH_CONFIGURATION_PROPERTY, mResourceFetchConfiguration);
        JsonUtils.add(builder, ALTERNATIVE_PROPERTY, mAlternative);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mExpression == null ? 0 : mExpression.hashCode());
        result = prime * result + (mPath == null ? 0 : mPath.hashCode());
        result = prime * result + (mResourceFetchConfiguration == null ? 0 : mResourceFetchConfiguration.hashCode());
        result = prime * result + (mAlternative == null ? 0 : mAlternative.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AudioFile other = (AudioFile) obj;
        if (mExpression == null) {
            if (other.mExpression != null) return false;
        } else if (!mExpression.equals(other.mExpression)) return false;
        if (mPath == null) {
            if (other.mPath != null) return false;
        } else if (!mPath.equals(other.mPath)) return false;
        if (mResourceFetchConfiguration == null) {
            if (other.mResourceFetchConfiguration != null) return false;
        } else if (!mResourceFetchConfiguration.equals(other.mResourceFetchConfiguration)) return false;
        if (mAlternative == null) {
            if (other.mAlternative != null) return false;
        } else if (!mAlternative.equals(other.mAlternative)) return false;
        return true;
    }

}