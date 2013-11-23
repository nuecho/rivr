/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A {@link AudioFile} represents an audio file with an optional synthesis text
 * backup.
 * 
 * @author Nu Echo Inc.
 * @see SpeechSynthesis
 * @see FetchConfiguration
 */
public final class AudioFile extends AudioItem {
    public static final String AUDIO_FILE_ELEMENT_TYPE = "audioFile";
    private static final String FETCH_CONFIGURATION_PROPERTY = "fetchConfiguration";
    private static final String LOCATION_PROPERTY = "location";
    private static final String EXPRESSION_PROPERTY = "expression";
    private static final String ALTERNATE_PROPERTY = "alternate";

    private String mLocation;
    private String mExpression;
    private SpeechSynthesis mAlternate;
    private FetchConfiguration mFetchConfiguration;

    public static AudioFile fromLocation(String location) {
        Assert.notEmpty(location, "location");
        AudioFile audioFile = new AudioFile();
        audioFile.mLocation = location;
        return audioFile;
    }

    public static AudioFile fromExpression(String expression) {
        Assert.notEmpty(expression, "expression");
        AudioFile audioFile = new AudioFile();
        audioFile.mExpression = expression;
        return audioFile;
    }

    public static AudioFile fromLocation(String location, SpeechSynthesis alternate) {
        AudioFile audioFile = fromLocation(location);
        audioFile.setAlternate(alternate);
        return audioFile;
    }

    public static AudioFile fromExpression(String expression, SpeechSynthesis alternate) {
        AudioFile audioFile = fromExpression(expression);
        audioFile.setAlternate(alternate);
        return audioFile;
    }

    public static AudioFile fromLocation(String location, String alternate) {
        AudioFile audioFile = fromLocation(location);
        audioFile.setAlternate(alternate);
        return audioFile;
    }

    public static AudioFile fromExpression(String expression, String alternate) {
        AudioFile audioFile = fromExpression(expression);
        audioFile.setAlternate(alternate);
        return audioFile;
    }

    /**
     * @param fetchConfiguration The resource fetch configuration.
     *            <code>null</code> to use the VoiceXML platform default.
     */
    public void setFetchConfiguration(FetchConfiguration fetchConfiguration) {
        mFetchConfiguration = fetchConfiguration;
    }

    public void setAlternate(SpeechSynthesis alternate) {
        mAlternate = alternate;
    }

    public void setAlternate(String alternate) {
        mAlternate = new SpeechSynthesis(alternate);
    }

    @Override
    public String getElementType() {
        return AUDIO_FILE_ELEMENT_TYPE;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getExpression() {
        return mExpression;
    }

    public SpeechSynthesis getAlternate() {
        return mAlternate;
    }

    public FetchConfiguration getFetchConfiguration() {
        return mFetchConfiguration;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, LOCATION_PROPERTY, mLocation);
        JsonUtils.add(builder, EXPRESSION_PROPERTY, mExpression);
        JsonUtils.add(builder, FETCH_CONFIGURATION_PROPERTY, mFetchConfiguration);
        JsonUtils.add(builder, ALTERNATE_PROPERTY, mAlternate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mExpression == null ? 0 : mExpression.hashCode());
        result = prime * result + (mLocation == null ? 0 : mLocation.hashCode());
        result = prime * result + (mFetchConfiguration == null ? 0 : mFetchConfiguration.hashCode());
        result = prime * result + (mAlternate == null ? 0 : mAlternate.hashCode());
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
        if (mLocation == null) {
            if (other.mLocation != null) return false;
        } else if (!mLocation.equals(other.mLocation)) return false;
        if (mFetchConfiguration == null) {
            if (other.mFetchConfiguration != null) return false;
        } else if (!mFetchConfiguration.equals(other.mFetchConfiguration)) return false;
        if (mAlternate == null) {
            if (other.mAlternate != null) return false;
        } else if (!mAlternate.equals(other.mAlternate)) return false;
        return true;
    }

}