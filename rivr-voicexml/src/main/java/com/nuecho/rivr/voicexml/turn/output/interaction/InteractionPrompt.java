/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import static java.util.Arrays.*;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * An <code>InteractionPrompt</code> represent a phase in an
 * {@link InteractionTurn} and is composed of a sequence of {@link AudioItem}
 * and optionally a speech and/or a dtmf recognition configuration.
 * 
 * @author Nu Echo Inc.
 */
public final class InteractionPrompt implements JsonSerializable {
    private static final String SPEECH_RECOGNITION_CONFIGURATION_PROPERTY = "speechRecognitionConfiguration";
    private static final String DTMF_RECOGNITION_CONFIGURATION_PROPERTY = "dtmfRecognitionConfiguration";
    private static final String BARGE_IN_TYPE_PROPERTY = "bargeInType";
    private static final String AUDIO_ITEMS_PROPERTY = "audioItems";
    private static final String LANGUAGE_PROPERTY = "language";

    private final List<AudioItem> mAudioItems;
    private final SpeechRecognitionConfiguration mSpeechRecognitionConfiguration;
    private final DtmfRecognitionConfiguration mDtmfRecognitionConfiguration;

    private String mLanguage;
    private BargeInType mBargeInType;

    /**
     * @param speechRecognitionConfiguration The speech recognition
     *            configuration. Optional.
     * @param dtmfRecognitionConfiguration The DTMF recognition configuration.
     *            Optional.
     * @param audioItems The list of {@link AudioItem}. Not null.
     */
    public InteractionPrompt(SpeechRecognitionConfiguration speechRecognitionConfiguration,
                             DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                             List<? extends AudioItem> audioItems) {
        Assert.notNull(audioItems, "audioItems");
        mAudioItems = new ArrayList<AudioItem>(audioItems);
        mSpeechRecognitionConfiguration = speechRecognitionConfiguration;
        mDtmfRecognitionConfiguration = dtmfRecognitionConfiguration;
    }

    /**
     * @param speechRecognitionConfiguration The speech recognition
     *            configuration.
     * @param dtmfRecognitionConfiguration The DTMF recognition configuration.
     * @param audioItems The list of {@link AudioItem}. Not null.
     */
    public InteractionPrompt(SpeechRecognitionConfiguration speechRecognitionConfiguration,
                             DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                             AudioItem... audioItems) {
        this(speechRecognitionConfiguration, dtmfRecognitionConfiguration, asList(audioItems));
    }

    /**
     * @param speechRecognitionConfiguration The speech recognition
     *            configuration.
     * @param audioItems The list of {@link AudioItem}. Not null.
     */
    public InteractionPrompt(SpeechRecognitionConfiguration speechRecognitionConfiguration,
                             List<? extends AudioItem> audioItems) {
        this(speechRecognitionConfiguration, null, audioItems);
    }

    /**
     * @param speechRecognitionConfiguration The speech recognition
     *            configuration.
     * @param audioItems The list of {@link AudioItem}. Not null.
     */
    public InteractionPrompt(SpeechRecognitionConfiguration speechRecognitionConfiguration, AudioItem... audioItems) {
        this(speechRecognitionConfiguration, null, audioItems);
    }

    /**
     * @param dtmfRecognitionConfiguration The DTMF recognition configuration.
     * @param audioItems The list of {@link AudioItem}. Not null.
     */
    public InteractionPrompt(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                             List<? extends AudioItem> audioItems) {
        this(null, dtmfRecognitionConfiguration, audioItems);
    }

    /**
     * @param dtmfRecognitionConfiguration The DTMF recognition configuration.
     * @param audioItems The list of {@link AudioItem}. Not null.
     */
    public InteractionPrompt(DtmfRecognitionConfiguration dtmfRecognitionConfiguration, AudioItem... audioItems) {
        this(null, dtmfRecognitionConfiguration, audioItems);
    }

    /**
     * @param audioItems The list of {@link AudioItem}. Not null.
     */
    public InteractionPrompt(List<? extends AudioItem> audioItems) {
        this(null, null, audioItems);
    }

    /**
     * @param audioItems The list of {@link AudioItem}. Not null.
     */
    public InteractionPrompt(AudioItem... audioItems) {
        this(null, null, audioItems);
    }

    /**
     * @param language language code for this prompt. <code>null</code> if
     *            language should be reset to platform-specific default value
     *            for the prompts to be added.
     */
    public void setLanguage(String language) {
        mLanguage = language;
    }

    /**
     * @param bargeInType {@link BargeInType#SPEECH} or
     *            {@link BargeInType#HOTWORD}. <code>null</code> if language
     *            should be reset to platform-specific default value for the
     *            prompts to be added.
     */
    public void setHotWordBargeIn(BargeInType bargeInType) {
        mBargeInType = bargeInType;
    }

    public List<? extends AudioItem> getAudioItems() {
        return Collections.unmodifiableList(mAudioItems);
    }

    public String getLanguage() {
        return mLanguage;
    }

    public SpeechRecognitionConfiguration getSpeechRecognitionConfiguration() {
        return mSpeechRecognitionConfiguration;
    }

    public DtmfRecognitionConfiguration getDtmfRecognitionConfiguration() {
        return mDtmfRecognitionConfiguration;
    }

    public BargeInType getBargeInType() {
        return mBargeInType;
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, LANGUAGE_PROPERTY, mLanguage);
        JsonUtils.add(builder, BARGE_IN_TYPE_PROPERTY, mBargeInType == null ? null : mBargeInType.getKey());
        JsonUtils.add(builder, AUDIO_ITEMS_PROPERTY, JsonUtils.toJson(mAudioItems));
        JsonUtils.add(builder, DTMF_RECOGNITION_CONFIGURATION_PROPERTY, mDtmfRecognitionConfiguration);
        JsonUtils.add(builder, SPEECH_RECOGNITION_CONFIGURATION_PROPERTY, mSpeechRecognitionConfiguration);
        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mAudioItems == null) ? 0 : mAudioItems.hashCode());
        result = prime * result + ((mBargeInType == null) ? 0 : mBargeInType.hashCode());
        result = prime
                 * result
                 + ((mDtmfRecognitionConfiguration == null) ? 0 : mDtmfRecognitionConfiguration.hashCode());
        result = prime * result + ((mLanguage == null) ? 0 : mLanguage.hashCode());
        result = prime
                 * result
                 + ((mSpeechRecognitionConfiguration == null) ? 0 : mSpeechRecognitionConfiguration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        InteractionPrompt other = (InteractionPrompt) obj;
        if (mAudioItems == null) {
            if (other.mAudioItems != null) return false;
        } else if (!mAudioItems.equals(other.mAudioItems)) return false;
        if (mBargeInType != other.mBargeInType) return false;
        if (mDtmfRecognitionConfiguration == null) {
            if (other.mDtmfRecognitionConfiguration != null) return false;
        } else if (!mDtmfRecognitionConfiguration.equals(other.mDtmfRecognitionConfiguration)) return false;
        if (mLanguage == null) {
            if (other.mLanguage != null) return false;
        } else if (!mLanguage.equals(other.mLanguage)) return false;
        if (mSpeechRecognitionConfiguration == null) {
            if (other.mSpeechRecognitionConfiguration != null) return false;
        } else if (!mSpeechRecognitionConfiguration.equals(other.mSpeechRecognitionConfiguration)) return false;
        return true;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }
}