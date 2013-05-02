/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class InteractionPrompt implements JsonSerializable {

    private static final String SPEECH_RECOGNITION_CONFIGURATION_PROPERTY = "speechRecognitionConfiguration";
    private static final String DTMF_RECOGNITION_CONFIGURATION_PROPERTY = "dtmfRecognitionConfiguration";
    private static final String HOT_WORD_BARGEIN_PROPERTY = "hotWordBargein";
    private static final String BARGEIN_PROPERTY = "bargein";
    private static final String AUDIO_ITEMS_PROPERTY = "audioItems";
    private static final String LANGUAGE_PROPERTY = "language";

    private final List<AudioItem> mAudioItems;
    private final String mLanguage;

    private final SpeechRecognitionConfiguration mSpeechRecognitionConfiguration;
    private final DtmfRecognitionConfiguration mDtmfRecognitionConfiguration;
    private final boolean mBargeIn;
    private boolean mHotWordBargeIn;

    public InteractionPrompt(List<? extends AudioItem> audioItems,
                             SpeechRecognitionConfiguration speechRecognitionConfiguration,
                             DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                             String language) {
        Assert.notNull(audioItems, "audioItems");
        mAudioItems = new ArrayList<AudioItem>(audioItems);
        mLanguage = language;
        mSpeechRecognitionConfiguration = speechRecognitionConfiguration;
        mDtmfRecognitionConfiguration = dtmfRecognitionConfiguration;
        mBargeIn = speechRecognitionConfiguration != null || dtmfRecognitionConfiguration != null;
    }

    public InteractionPrompt(List<? extends AudioItem> audioItems, boolean bargeIn, String language) {
        Assert.notNull(audioItems, "audioItems");
        mAudioItems = new ArrayList<AudioItem>(audioItems);
        mLanguage = language;
        mSpeechRecognitionConfiguration = null;
        mDtmfRecognitionConfiguration = null;
        mBargeIn = bargeIn;
    }

    public boolean isBargeIn() {
        return mBargeIn;
    }

    public boolean isHotWordBargeIn() {
        return mHotWordBargeIn;
    }

    public void setHotWordBargeIn(boolean hotWordBargeIn) {
        mHotWordBargeIn = hotWordBargeIn;
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

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, LANGUAGE_PROPERTY, mLanguage);
        JsonUtils.add(builder, AUDIO_ITEMS_PROPERTY, JsonUtils.toJson(mAudioItems));
        JsonUtils.addBooleanProperty(builder, BARGEIN_PROPERTY, mBargeIn);
        JsonUtils.addBooleanProperty(builder, HOT_WORD_BARGEIN_PROPERTY, mHotWordBargeIn);
        JsonUtils.add(builder, DTMF_RECOGNITION_CONFIGURATION_PROPERTY, mDtmfRecognitionConfiguration);
        JsonUtils.add(builder, SPEECH_RECOGNITION_CONFIGURATION_PROPERTY, mSpeechRecognitionConfiguration);
        return builder.build();
    }

    @Override
    public String toString() {
        return asJson().toString();
    }
}