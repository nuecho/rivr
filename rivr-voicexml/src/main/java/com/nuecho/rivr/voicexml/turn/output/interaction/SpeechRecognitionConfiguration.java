/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.grammar.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>SpeechRecognitionConfiguration</code> represents the speech part of
 * an interaction phase recognition configuration. It is composed of a list of
 * speech {@link GrammarItem} and optional properties (complete timeout,
 * incomplete timeout, max speech timeout, max nbest, confidence level, speed
 * versus accuracy and sensitivity).
 * 
 * @author Nu Echo Inc.
 */
public final class SpeechRecognitionConfiguration extends RecognitionConfiguration {

    private static final String SPEED_VERSUS_ACCURACY_PROPERTY = "speedVersusAccuracy";
    private static final String SENSITIVITY_PROPERTY = "sensitivity";
    private static final String MAX_N_BEST_PROPERTY = "maxNBest";
    private static final String CONFIDENCE_LEVEL_PROPERTY = "confidenceLevel";
    private static final String MAX_SPEECH_TIMEOUT_PROPERTY = "maxSpeechTimeout";
    private static final String INCOMPLETE_TIMEOUT_PROPERTY = "incompleteTimeout";
    private static final String COMPLETE_TIMEOUT_PROPERTY = "completeTimeout";
    private TimeValue mCompleteTimeout;
    private TimeValue mIncompleteTimeout;
    private TimeValue mMaxSpeechTimeout;
    private Integer mMaxNBest;

    private Double mConfidenceLevel;
    private Double mSpeedVersusAccuracy;
    private Double mSensitivity;

    /**
     * @param grammarItems The list of speech {@link GrammarItem}. Not null.
     */
    public SpeechRecognitionConfiguration(GrammarItem... grammarItems) {
        super(grammarItems);
    }

    /**
     * @param grammarItems The list of speech {@link GrammarItem}. Not null.
     */
    public SpeechRecognitionConfiguration(List<GrammarItem> grammarItems) {
        super(grammarItems);
    }

    /**
     * @param completeTimeout The required length of silence following user
     *            speech before the speech recognizer finalizes a complete
     *            result. Null reverts to VoiceXML default value.
     * @see <a href="http://www.w3.org/TR/voicexml20/#dml6.3.2">http://www.w3.org/TR/voicexml20/#dml6.3.2</a>
     */
    public void setCompleteTimeout(TimeValue completeTimeout) {
        mCompleteTimeout = completeTimeout;
    }

    /**
     * @param incompleteTimeout The required length of silence following user
     *            speech after which a recognizer finalizes an incomplete
     *            result. Null reverts to VoiceXML default value.
     * @see <a href="http://www.w3.org/TR/voicexml20/#dml6.3.2">http://www.w3.org/TR/voicexml20/#dml6.3.2</a>
     */
    public void setIncompleteTimeout(TimeValue incompleteTimeout) {
        mIncompleteTimeout = incompleteTimeout;
    }

    /**
     * @param maxSpeechTimeout The maximum duration of user speech. Null reverts
     *            to VoiceXML default value.
     * @see <a href="http://www.w3.org/TR/voicexml20/#dml6.3.2">http://www.w3.org/TR/voicexml20/#dml6.3.2</a>
     */
    public void setMaxSpeechTimeout(TimeValue maxSpeechTimeout) {
        mMaxSpeechTimeout = maxSpeechTimeout;
    }

    /**
     * @param maxNBest The maximum size of the recognition result. Must be a
     *            positive integer. Null reverts to VoiceXML default value.
     * @see <a href="http://www.w3.org/TR/voicexml20/#dml6.3.2">http://www.w3.org/TR/voicexml20/#dml6.3.2</a>
     */
    public void setMaxNBest(Integer maxNBest) {
        if (maxNBest != null) {
            Assert.positive(maxNBest, "maxNBest");
        }

        mMaxNBest = maxNBest;
    }

    /**
     * @param confidenceLevel The speech recognition confidence level. Results
     *            with confidence below this value will be rejected (nomatch).
     *            Value must be between 0.0 and 1.0. Null reverts to VoiceXML
     *            default value.
     * @see <a href="http://www.w3.org/TR/voicexml20/#dml6.3.2">http://www.w3.org/TR/voicexml20/#dml6.3.2</a>
     */
    public void setConfidenceLevel(Double confidenceLevel) {
        if (confidenceLevel != null) {
            Assert.between(0, confidenceLevel, 1.0);
        }

        mConfidenceLevel = confidenceLevel;
    }

    /**
     * @param speedVersusAccuracy A hint specifying the desired balance between
     *            speed versus accuracy. A value of 0.0 means fastest
     *            recognition. A value of 1.0 means best accuracy. Null reverts
     *            to VoiceXML default value.
     * @see <a href="http://www.w3.org/TR/voicexml20/#dml6.3.2">http://www.w3.org/TR/voicexml20/#dml6.3.2</a>
     */
    public void setSpeedVersusAccuracy(Double speedVersusAccuracy) {
        if (speedVersusAccuracy != null) {
            Assert.between(0, speedVersusAccuracy, 1.0);
        }

        mSpeedVersusAccuracy = speedVersusAccuracy;
    }

    /**
     * @param sensitivity Set the sensitivity level. A value of 1.0 means that
     *            it is highly sensitive to quiet input. A value of 0.0 means it
     *            is least sensitive to noise. Null reverts to VoiceXML default
     *            value.
     */
    public void setSensitivity(Double sensitivity) {
        if (sensitivity != null) {
            Assert.between(0, sensitivity, 1.0);
        }

        mSensitivity = sensitivity;
    }

    public TimeValue getCompleteTimeout() {
        return mCompleteTimeout;
    }

    public TimeValue getIncompleteTimeout() {
        return mIncompleteTimeout;
    }

    public TimeValue getMaxSpeechTimeout() {
        return mMaxSpeechTimeout;
    }

    public Integer getMaxNBest() {
        return mMaxNBest;
    }

    public Double getConfidenceLevel() {
        return mConfidenceLevel;
    }

    public Double getSpeedVersusAccuracy() {
        return mSpeedVersusAccuracy;
    }

    public Double getSensitivity() {
        return mSensitivity;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.addTimeProperty(builder, COMPLETE_TIMEOUT_PROPERTY, mCompleteTimeout);
        JsonUtils.addTimeProperty(builder, INCOMPLETE_TIMEOUT_PROPERTY, mIncompleteTimeout);
        JsonUtils.addTimeProperty(builder, MAX_SPEECH_TIMEOUT_PROPERTY, mMaxSpeechTimeout);
        JsonUtils.addNumberProperty(builder, CONFIDENCE_LEVEL_PROPERTY, mConfidenceLevel);
        JsonUtils.addNumberProperty(builder, MAX_N_BEST_PROPERTY, mMaxNBest);
        JsonUtils.addNumberProperty(builder, SENSITIVITY_PROPERTY, mSensitivity);
        JsonUtils.addNumberProperty(builder, SPEED_VERSUS_ACCURACY_PROPERTY, mSpeedVersusAccuracy);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mCompleteTimeout == null ? 0 : mCompleteTimeout.hashCode());
        result = prime * result + (mMaxNBest == null ? 0 : mMaxNBest.hashCode());
        result = prime * result + (mIncompleteTimeout == null ? 0 : mIncompleteTimeout.hashCode());
        result = prime * result + (mMaxSpeechTimeout == null ? 0 : mMaxSpeechTimeout.hashCode());
        result = prime * result + (mConfidenceLevel == null ? 0 : mConfidenceLevel.hashCode());
        result = prime * result + (mSensitivity == null ? 0 : mSensitivity.hashCode());
        result = prime * result + (mSpeedVersusAccuracy == null ? 0 : mSpeedVersusAccuracy.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SpeechRecognitionConfiguration other = (SpeechRecognitionConfiguration) obj;
        if (mCompleteTimeout == null) {
            if (other.mCompleteTimeout != null) return false;
        } else if (!mCompleteTimeout.equals(other.mCompleteTimeout)) return false;
        if (mMaxNBest == null) {
            if (other.mMaxNBest != null) return false;
        } else if (!mMaxNBest.equals(other.mMaxNBest)) return false;
        if (mIncompleteTimeout == null) {
            if (other.mIncompleteTimeout != null) return false;
        } else if (!mIncompleteTimeout.equals(other.mIncompleteTimeout)) return false;
        if (mMaxSpeechTimeout == null) {
            if (other.mMaxSpeechTimeout != null) return false;
        } else if (!mMaxSpeechTimeout.equals(other.mMaxSpeechTimeout)) return false;
        if (mConfidenceLevel == null) {
            if (other.mConfidenceLevel != null) return false;
        } else if (!mConfidenceLevel.equals(other.mConfidenceLevel)) return false;
        if (mSensitivity == null) {
            if (other.mSensitivity != null) return false;
        } else if (!mSensitivity.equals(other.mSensitivity)) return false;
        if (mSpeedVersusAccuracy == null) {
            if (other.mSpeedVersusAccuracy != null) return false;
        } else if (!mSpeedVersusAccuracy.equals(other.mSpeedVersusAccuracy)) return false;
        return true;
    }

    public SpeechRecognitionConfiguration copy() {
        SpeechRecognitionConfiguration copy = new SpeechRecognitionConfiguration(getGrammarItems());
        copy.mCompleteTimeout = mCompleteTimeout;
        copy.mMaxNBest = mMaxNBest;
        copy.mIncompleteTimeout = mIncompleteTimeout;
        copy.mMaxSpeechTimeout = mMaxSpeechTimeout;
        copy.mConfidenceLevel = mConfidenceLevel;
        copy.mSensitivity = mSensitivity;
        copy.mSpeedVersusAccuracy = mSpeedVersusAccuracy;
        copyPropertiesTo(copy);
        return copy;
    }
}