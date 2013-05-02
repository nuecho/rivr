/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import java.util.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;


/**
 * @author Nu Echo Inc.
 */
public final class InteractionBuilder {

    private final String mName;
    private final List<InteractionPrompt> mPrompts = new ArrayList<InteractionPrompt>();
    private String mLanguage;
    private boolean mHotWordBargeIn;

    private InteractionRecognition mInteractionRecognition;
    private InteractionRecording mInteractionRecording;

    public InteractionBuilder(String name) {
        mName = name;
    }

    public void setHotWordBargeIn(boolean hotWordBargeIn) {
        mHotWordBargeIn = hotWordBargeIn;
    }

    /**
     * Adds a non-interruptible prompt. This is equivalent to call
     * <code>addPrompt(audioItems, false)</code>.
     */
    public void addPrompt(AudioItem... audioItems) {
        addPrompt(Arrays.asList(audioItems));
    }

    public void addPrompt(List<? extends AudioItem> audioItems) {
        addPrompt(audioItems, false);
    }

    /**
     * @param dtmfRecognitionConfiguration Can be <code>null</code> if no DTMF
     *            can be recognized during this prompt.
     * @param speechRecognitionConfiguration Can be <code>null</code> if no
     *            speech can be recognized during this prompt.
     */
    public void addPrompt(List<? extends AudioItem> audioItems,
                          DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                          SpeechRecognitionConfiguration speechRecognitionConfiguration) {
        InteractionPrompt prompt = new InteractionPrompt(audioItems,
                                                         speechRecognitionConfiguration,
                                                         dtmfRecognitionConfiguration,
                                                         mLanguage);
        prompt.setHotWordBargeIn(mHotWordBargeIn);
        mPrompts.add(prompt);
    }

    public void addPrompt(List<? extends AudioItem> audioItems, boolean bargeIn) {
        InteractionPrompt prompt = new InteractionPrompt(audioItems, bargeIn, mLanguage);
        prompt.setHotWordBargeIn(mHotWordBargeIn);
        mPrompts.add(prompt);
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public void removeLanguage() {
        mLanguage = null;
    }

    public void setFinalRecognition(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                    SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                    TimeValue noinputTimeout,
                                    AudioItem... acknowledgeAudioItems) {
        mInteractionRecognition = new InteractionRecognition(dtmfRecognitionConfiguration,
                                                             speechRecognitionConfiguration,
                                                             noinputTimeout,
                                                             acknowledgeAudioItems);

    }

    public void setFinalRecognition(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                    SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                    TimeValue noinputTimeout,
                                    List<? extends AudioItem> acknowledgeAudioItems) {
        mInteractionRecognition = new InteractionRecognition(dtmfRecognitionConfiguration,
                                                             speechRecognitionConfiguration,
                                                             noinputTimeout,
                                                             acknowledgeAudioItems);

    }

    public void setFinalRecording(RecordingConfiguration recordingConfiguration,
                                  TimeValue noinputTimeout,
                                  AudioItem... acknowledgeAudioItems) {
        mInteractionRecording = new InteractionRecording(recordingConfiguration, noinputTimeout, acknowledgeAudioItems);
    }

    public void setFinalRecording(RecordingConfiguration recordingConfiguration,
                                  TimeValue noinputTimeout,
                                  List<? extends AudioItem> acknowledgeAudioItems) {
        mInteractionRecording = new InteractionRecording(recordingConfiguration, noinputTimeout, acknowledgeAudioItems);
    }

    public InteractionTurn build() {
        return new InteractionTurn(mName, mPrompts, mInteractionRecognition, mInteractionRecording);
    }
}