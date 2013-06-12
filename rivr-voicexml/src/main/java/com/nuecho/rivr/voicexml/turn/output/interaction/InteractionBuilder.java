/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import static java.util.Arrays.*;

import java.util.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;

/**
 * @author Nu Echo Inc.
 */
public final class InteractionBuilder {

    private final String mName;
    private final List<InteractionPrompt> mPrompts = new ArrayList<InteractionPrompt>();
    private InteractionRecognition mInteractionRecognition;
    private InteractionRecording mInteractionRecording;

    private String mLanguage;
    private boolean mHotWordBargeIn;

    public static InteractionBuilder newBuilder(String name) {
        return new InteractionBuilder(name);
    }

    private InteractionBuilder(String name) {
        mName = name;
    }

    public InteractionBuilder setHotWordBargeIn(boolean hotWordBargeIn) {
        mHotWordBargeIn = hotWordBargeIn;
        return this;
    }

    public InteractionBuilder addPrompt(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                        SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                        AudioItem... audioItems) {
        return addPrompt(dtmfRecognitionConfiguration, speechRecognitionConfiguration, asList(audioItems));
    }

    /**
     * @param dtmfRecognitionConfiguration Can be <code>null</code> if no DTMF
     *            can be recognized during this prompt.
     * @param speechRecognitionConfiguration Can be <code>null</code> if no
     *            speech can be recognized during this prompt.
     */
    public InteractionBuilder addPrompt(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                        SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                        List<? extends AudioItem> audioItems) {
        InteractionPrompt prompt = new InteractionPrompt(audioItems,
                                                         speechRecognitionConfiguration,
                                                         dtmfRecognitionConfiguration,
                                                         mLanguage);
        prompt.setHotWordBargeIn(mHotWordBargeIn);
        mPrompts.add(prompt);
        return this;
    }

    public InteractionBuilder addPrompt(AudioItem... audioItems) {
        return addPrompt(Arrays.asList(audioItems));
    }

    public InteractionBuilder addPrompt(List<? extends AudioItem> audioItems) {
        InteractionPrompt prompt = new InteractionPrompt(audioItems, mLanguage);
        prompt.setHotWordBargeIn(mHotWordBargeIn);
        mPrompts.add(prompt);
        return this;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public InteractionBuilder setLanguage(String language) {
        mLanguage = language;
        return this;
    }

    public InteractionBuilder resetLanguage() {
        mLanguage = null;
        return this;
    }

    public InteractionBuilder setFinalRecognition(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                                  SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                                  TimeValue noinputTimeout,
                                                  AudioItem... acknowledgeAudioItems) {

        return setFinalRecognition(dtmfRecognitionConfiguration,
                                   speechRecognitionConfiguration,
                                   noinputTimeout,
                                   asList(acknowledgeAudioItems));

    }

    public InteractionBuilder setFinalRecognition(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                                  SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                                  TimeValue noinputTimeout,
                                                  List<? extends AudioItem> acknowledgeAudioItems) {

        Assert.ensure(dtmfRecognitionConfiguration != null || speechRecognitionConfiguration != null,
                      "Must provide at least one recognition configuration (speech or DTMF)");

        if (mInteractionRecording != null)
            throw new IllegalStateException("Cannot set final recognition while final recording is set");

        mInteractionRecognition = new InteractionRecognition(dtmfRecognitionConfiguration,
                                                             speechRecognitionConfiguration,
                                                             noinputTimeout,
                                                             acknowledgeAudioItems);
        return this;

    }

    public InteractionBuilder setFinalRecording(RecordingConfiguration recordingConfiguration,
                                                TimeValue noinputTimeout,
                                                AudioItem... acknowledgeAudioItems) {
        return setFinalRecording(recordingConfiguration, noinputTimeout, asList(acknowledgeAudioItems));
    }

    public InteractionBuilder setFinalRecording(RecordingConfiguration recordingConfiguration,
                                                TimeValue noinputTimeout,
                                                List<? extends AudioItem> acknowledgeAudioItems) {
        Assert.notNull(recordingConfiguration, "recordingConfiguration");

        if (mInteractionRecognition != null)
            throw new IllegalStateException("Cannot set final recording while final recognition is set");

        mInteractionRecording = new InteractionRecording(recordingConfiguration, noinputTimeout, acknowledgeAudioItems);
        return this;
    }

    public InteractionTurn build() {
        return new InteractionTurn(mName, mPrompts, mInteractionRecognition, mInteractionRecording);
    }
}