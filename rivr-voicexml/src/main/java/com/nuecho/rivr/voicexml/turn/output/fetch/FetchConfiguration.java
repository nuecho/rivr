/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.fetch;

import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class FetchConfiguration {

    private AudioFetchConfiguration mAudioFetchConfiguration = new AudioFetchConfiguration();
    private GrammarFetchConfiguration mGrammarFetchConfiguration = new GrammarFetchConfiguration();
    private ScriptFetchConfiguration mScriptFetchConfiguration = new ScriptFetchConfiguration();
    private ObjectFetchConfiguration mObjectFetchConfiguration = new ObjectFetchConfiguration();

    private DocumentFetchConfiguration mDocumentFetchConfiguration = new DocumentFetchConfiguration();

    private Duration mDefaultFetchTimeout;
    private String mDefaultFetchAudio;

    public AudioFetchConfiguration getDefaultAudioFetchConfiguration() {
        return mAudioFetchConfiguration;
    }

    public void setDefaultAudioFetchConfiguration(AudioFetchConfiguration audioFetchConfiguration) {
        mAudioFetchConfiguration = audioFetchConfiguration;
    }

    public GrammarFetchConfiguration getDefaultGrammarFetchConfiguration() {
        return mGrammarFetchConfiguration;
    }

    public void setDefaultGrammarFetchConfiguration(GrammarFetchConfiguration grammarFetchConfiguration) {
        mGrammarFetchConfiguration = grammarFetchConfiguration;
    }

    public ScriptFetchConfiguration getDefaultScriptFetchConfiguration() {
        return mScriptFetchConfiguration;
    }

    public void setDefaultScriptFetchConfiguration(ScriptFetchConfiguration scriptFetchConfiguration) {
        mScriptFetchConfiguration = scriptFetchConfiguration;
    }

    public ObjectFetchConfiguration getDefaultObjectFetchConfiguration() {
        return mObjectFetchConfiguration;
    }

    public void setDefaultObjectFetchConfiguration(ObjectFetchConfiguration objectFetchConfiguration) {
        mObjectFetchConfiguration = objectFetchConfiguration;
    }

    public Duration getDefaultFetchTimeout() {
        return mDefaultFetchTimeout;
    }

    public void setDefaultFetchTimeout(Duration defaultFetchTimeout) {
        mDefaultFetchTimeout = defaultFetchTimeout;
    }

    public String getDefaultFetchAudio() {
        return mDefaultFetchAudio;
    }

    public void setDefaultFetchAudio(String defaultFetchAudio) {
        mDefaultFetchAudio = defaultFetchAudio;
    }

    public DocumentFetchConfiguration getDocumentFetchConfiguration() {
        return mDocumentFetchConfiguration;
    }

    public void setDocumentFetchConfiguration(DocumentFetchConfiguration documentFetchConfiguration) {
        mDocumentFetchConfiguration = documentFetchConfiguration;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mAudioFetchConfiguration == null) ? 0 : mAudioFetchConfiguration.hashCode());
        result = prime * result + ((mDefaultFetchAudio == null) ? 0 : mDefaultFetchAudio.hashCode());
        result = prime * result + ((mDefaultFetchTimeout == null) ? 0 : mDefaultFetchTimeout.hashCode());
        result = prime * result + ((mGrammarFetchConfiguration == null) ? 0 : mGrammarFetchConfiguration.hashCode());
        result = prime * result + ((mObjectFetchConfiguration == null) ? 0 : mObjectFetchConfiguration.hashCode());
        result = prime * result + ((mScriptFetchConfiguration == null) ? 0 : mScriptFetchConfiguration.hashCode());
        result = prime * result + ((mDocumentFetchConfiguration == null) ? 0 : mDocumentFetchConfiguration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FetchConfiguration other = (FetchConfiguration) obj;
        if (mAudioFetchConfiguration == null) {
            if (other.mAudioFetchConfiguration != null) return false;
        } else if (!mAudioFetchConfiguration.equals(other.mAudioFetchConfiguration)) return false;
        if (mDefaultFetchAudio == null) {
            if (other.mDefaultFetchAudio != null) return false;
        } else if (!mDefaultFetchAudio.equals(other.mDefaultFetchAudio)) return false;
        if (mDefaultFetchTimeout == null) {
            if (other.mDefaultFetchTimeout != null) return false;
        } else if (!mDefaultFetchTimeout.equals(other.mDefaultFetchTimeout)) return false;
        if (mGrammarFetchConfiguration == null) {
            if (other.mGrammarFetchConfiguration != null) return false;
        } else if (!mGrammarFetchConfiguration.equals(other.mGrammarFetchConfiguration)) return false;
        if (mObjectFetchConfiguration == null) {
            if (other.mObjectFetchConfiguration != null) return false;
        } else if (!mObjectFetchConfiguration.equals(other.mObjectFetchConfiguration)) return false;
        if (mScriptFetchConfiguration == null) {
            if (other.mScriptFetchConfiguration != null) return false;
        } else if (!mScriptFetchConfiguration.equals(other.mScriptFetchConfiguration)) return false;
        if (mDocumentFetchConfiguration == null) {
            if (other.mDocumentFetchConfiguration != null) return false;
        } else if (!mDocumentFetchConfiguration.equals(other.mDocumentFetchConfiguration)) return false;
        return true;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendItem("mDocumentFetchConfiguration", mDocumentFetchConfiguration);
        builder.appendItem("mDefaultFetchAudio", mDefaultFetchAudio);
        builder.appendItem("mDefaultFetchTimeout", mDefaultFetchTimeout);
        builder.appendItem("mAudioFetchConfiguration", mAudioFetchConfiguration);
        builder.appendItem("mGrammarFetchConfiguration", mGrammarFetchConfiguration);
        builder.appendItem("mObjectFetchConfiguration", mObjectFetchConfiguration);
        builder.appendItem("mScriptFetchConfiguration", mScriptFetchConfiguration);
        return builder.getString();
    }
}