/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import static java.util.Arrays.*;

import java.util.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;

/**
 * Builder used to ease creation of {@link InteractionTurn}
 * <p>
 * Building an {@link InteractionTurn} implies the following steps:
 * <ul>
 * <li>Add some prompts</li>
 * <ul>
 * <li>For each prompt, speech/DTMF recognition can be specified (thus enabling
 * <i>barge-in</i> when the prompt is played).
 * </ul>
 * <li>Once all prompts are added, optionnally specify either:
 * <ul>
 * <li>a final recognition windows (speech or DTMF)</li>
 * <li>a recording</li>
 * </ul>
 * </ul>
 * <p>
 * At any time, it is possible to change the current language used for prompts
 * (relevant for speech synthesis) and the barge-in type, i.e. <i>speech</i>
 * (default) or <i>hotword</i>.
 * <p>
 * This can be translated to:
 * 
 * <pre>
 *  InteractionBuilder builder = InteractionBuilder.newInteractionBuilder();
 *  builder.addPrompt(...);
 *  builder.addPrompt(...);
 *  //repeat as needed
 *  builder.addPrompt(...);
 *  InteractionTurn interactionTurn = builder.build(...);
 * </pre>
 * 
 * @author Nu Echo Inc.
 */
public final class InteractionBuilder {

    private final String mName;
    private final List<InteractionPrompt> mPrompts = new ArrayList<InteractionPrompt>();
    private InteractionRecognition mInteractionRecognition;
    private InteractionRecording mInteractionRecording;

    private String mLanguage;
    private boolean mHotWordBargeIn;

    private boolean mBuilt;

    private InteractionBuilder(String name) {
        mName = name;
    }

    /**
     * Creates an InteractionBuilder.
     * 
     * @param name of the interaction to be created.
     */

    public static InteractionBuilder newInteractionBuilder(String name) {
        return new InteractionBuilder(name);
    }

    /**
     * Sets the barge-in type to "hotword" for the prompts that will be added
     * using one of the <code>addPrompt(...)</code> methods.
     * <p>
     * Note: When an <code>InteractionBuilder</code> is created, the default
     * value for this flag is <code>false</code>, meaning the barge-in type will
     * be "speech".
     * 
     * @param hotWordBargeIn <code>true</code> to set barge-in type to
     *            "hotword". <code>false</code> to set barge-in type to
     *            "speech".
     */
    public InteractionBuilder setHotWordBargeIn(boolean hotWordBargeIn) {
        mHotWordBargeIn = hotWordBargeIn;
        return this;
    }

    /**
     * Sets the language code for the prompts that will be added using one of
     * the <code>addPrompt(...)</code> methods.
     * <p>
     * Note: When an <code>InteractionBuilder</code> is created, the default
     * value for this property is <code>null</code>, i.e. no explicit language
     * code will be generated in the VoiceXML thus relying on the
     * platform-specific default language code.
     * 
     * @param language language code for this prompt. <code>null</code> if
     *            language should be reset to platform-specific default value
     *            for the prompts to be added.
     */
    public InteractionBuilder setLanguage(String language) {
        mLanguage = language;
        return this;
    }

    /**
     * Add a prompt with DTMF recognition only.
     * 
     * @param dtmfRecognitionConfiguration configuration for the DTMF
     *            recognition
     * @param audioItems audio items to be played during this prompt.
     */
    public InteractionBuilder addPrompt(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                        AudioItem... audioItems) {
        return addPrompt(dtmfRecognitionConfiguration, null, asList(audioItems));
    }

    /**
     * Add a prompt with DTMF recognition only.
     * 
     * @param dtmfRecognitionConfiguration configuration for the DTMF
     *            recognition
     * @param audioItems audio items to be played during this prompt.
     */
    public InteractionBuilder addPrompt(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                        List<? extends AudioItem> audioItems) {
        return addPrompt(dtmfRecognitionConfiguration, null, audioItems);
    }

    /**
     * Add a prompt with speech recognition only.
     * 
     * @param speechRecognitionConfiguration configuration for the speech
     *            recognition
     * @param audioItems audio items to be played during this prompt.
     */
    public InteractionBuilder addPrompt(SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                        AudioItem... audioItems) {
        return addPrompt(null, speechRecognitionConfiguration, asList(audioItems));
    }

    /**
     * Add a prompt with speech recognition only.
     * 
     * @param speechRecognitionConfiguration configuration for the speech
     *            recognition
     * @param audioItems audio items to be played during this prompt.
     */
    public InteractionBuilder addPrompt(SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                        List<? extends AudioItem> audioItems) {
        return addPrompt(null, speechRecognitionConfiguration, audioItems);
    }

    /**
     * Add a prompt with both DTMF and speech recognition.
     * 
     * @param speechRecognitionConfiguration configuration for the speech
     *            recognition or <code>null</code> to disable DTMF recognition.
     * @param dtmfRecognitionConfiguration configuration for the DTMF
     *            recognition or <code>null</code> to disable DTMF recognition.
     * @param audioItems audio items to be played during this prompt.
     */
    public InteractionBuilder addPrompt(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                        SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                        AudioItem... audioItems) {
        return addPrompt(dtmfRecognitionConfiguration, speechRecognitionConfiguration, asList(audioItems));
    }

    /**
     * Add a prompt with both DTMF and speech recognition.
     * 
     * @param speechRecognitionConfiguration configuration for the speech
     *            recognition or <code>null</code> to disable DTMF recognition.
     * @param dtmfRecognitionConfiguration configuration for the DTMF
     *            recognition or <code>null</code> to disable DTMF recognition.
     * @param audioItems audio items to be played during this prompt.
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

    /**
     * Add a prompt without any DTMF nor speech recognition (no barge-in).
     * 
     * @param audioItems audio items to be played during this prompt.
     */
    public InteractionBuilder addPrompt(AudioItem... audioItems) {
        return addPrompt(Arrays.asList(audioItems));
    }

    /**
     * Add a prompt without any DTMF nor speech recognition (no barge-in).
     * 
     * @param audioItems audio items to be played during this prompt.
     */
    public InteractionBuilder addPrompt(List<? extends AudioItem> audioItems) {
        InteractionPrompt prompt = new InteractionPrompt(audioItems, mLanguage);
        prompt.setHotWordBargeIn(mHotWordBargeIn);
        mPrompts.add(prompt);
        return this;
    }

    /**
     * Build the interaction. This method will add a DTMF recognition window
     * after the prompts.
     * 
     * @param dtmfRecognitionConfiguration configuration for the DTMF
     *            recognition
     * @param noinputTimeout timeout value before a <code>noinput</code> is
     *            generated.
     * @param acknowledgeAudioItems audioItems to be played upon recognition
     */
    public InteractionTurn build(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                 TimeValue noinputTimeout,
                                 AudioItem... acknowledgeAudioItems) {

        return build(dtmfRecognitionConfiguration, null, noinputTimeout, asList(acknowledgeAudioItems));

    }

    /**
     * Build the interaction. This method will add a speech recognition window
     * after the prompts.
     * 
     * @param speechRecognitionConfiguration configuration for the speech
     *            recognition
     * @param noinputTimeout timeout value before a <code>noinput</code> is
     *            generated.
     * @param acknowledgeAudioItems audioItems to be played upon recognition
     */
    public InteractionTurn build(SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                 TimeValue noinputTimeout,
                                 AudioItem... acknowledgeAudioItems) {

        return build(null, speechRecognitionConfiguration, noinputTimeout, asList(acknowledgeAudioItems));

    }

    /**
     * Build the interaction. This method will add a DTMF recognition window
     * after the prompts.
     * 
     * @param dtmfRecognitionConfiguration configuration for the DTMF
     *            recognition
     * @param noinputTimeout timeout value before a <code>noinput</code> is
     *            generated.
     * @param acknowledgeAudioItems audioItems to be played upon recognition
     */
    public InteractionTurn build(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                 TimeValue noinputTimeout,
                                 List<? extends AudioItem> acknowledgeAudioItems) {
        return build(dtmfRecognitionConfiguration, null, noinputTimeout, acknowledgeAudioItems);

    }

    /**
     * Build the interaction. This method will add a speech recognition window
     * after the prompts.
     * 
     * @param speechRecognitionConfiguration configuration for the speech
     *            recognition
     * @param noinputTimeout timeout value before a <code>noinput</code> is
     *            generated.
     * @param acknowledgeAudioItems audioItems to be played upon recognition
     */
    public InteractionTurn build(SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                 TimeValue noinputTimeout,
                                 List<? extends AudioItem> acknowledgeAudioItems) {
        return build(null, speechRecognitionConfiguration, noinputTimeout, acknowledgeAudioItems);

    }

    /**
     * Build the interaction. This method will add a speech and DTMF recognition
     * window after the prompts.
     * 
     * @param speechRecognitionConfiguration configuration for the speech
     *            recognition
     * @param dtmfRecognitionConfiguration configuration for the DTMF
     *            recognition
     * @param noinputTimeout timeout value before a <code>noinput</code> is
     *            generated.
     * @param acknowledgeAudioItems audioItems to be played upon recognition
     */
    public InteractionTurn build(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                 SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                 TimeValue noinputTimeout,
                                 AudioItem... acknowledgeAudioItems) {

        return build(dtmfRecognitionConfiguration,
                     speechRecognitionConfiguration,
                     noinputTimeout,
                     asList(acknowledgeAudioItems));

    }

    /**
     * Build the interaction. This method will add a speech and DTMF recognition
     * window after the prompts.
     * 
     * @param speechRecognitionConfiguration configuration for the speech
     *            recognition
     * @param dtmfRecognitionConfiguration configuration for the DTMF
     *            recognition
     * @param noinputTimeout timeout value before a <code>noinput</code> is
     *            generated.
     * @param acknowledgeAudioItems audioItems to be played upon recognition
     */
    public InteractionTurn build(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                 SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                 TimeValue noinputTimeout,
                                 List<? extends AudioItem> acknowledgeAudioItems) {

        Assert.ensure(dtmfRecognitionConfiguration != null || speechRecognitionConfiguration != null,
                      "Must provide at least one recognition configuration (speech or DTMF)");

        mInteractionRecognition = new InteractionRecognition(dtmfRecognitionConfiguration,
                                                             speechRecognitionConfiguration,
                                                             noinputTimeout,
                                                             acknowledgeAudioItems);
        return build();

    }

    /**
     * Build the interaction. This method will add a recording window after the
     * prompts.
     * 
     * @param recordingConfiguration configuration for the recording
     * @param noinputTimeout timeout value before a <code>noinput</code> is
     *            generated.
     * @param acknowledgeAudioItems audioItems to be played upon recording
     *            completion
     */
    public InteractionTurn build(RecordingConfiguration recordingConfiguration,
                                 TimeValue noinputTimeout,
                                 AudioItem... acknowledgeAudioItems) {
        return build(recordingConfiguration, noinputTimeout, asList(acknowledgeAudioItems));
    }

    /**
     * Build the interaction. This method will add a recording window after the
     * prompts.
     * 
     * @param recordingConfiguration configuration for the recording
     * @param noinputTimeout timeout value before a <code>noinput</code> is
     *            generated.
     * @param acknowledgeAudioItems audioItems to be played upon recording
     *            completion
     */
    public InteractionTurn build(RecordingConfiguration recordingConfiguration,
                                 TimeValue noinputTimeout,
                                 List<? extends AudioItem> acknowledgeAudioItems) {
        Assert.notNull(recordingConfiguration, "recordingConfiguration");
        Assert.notNull(acknowledgeAudioItems, "acknowledgeAudioItems");
        mInteractionRecording = new InteractionRecording(recordingConfiguration, noinputTimeout, acknowledgeAudioItems);
        return build();
    }

    /**
     * Build the interaction. This method will not allow any recording nor
     * recognition after the prompts
     */
    public InteractionTurn build() {
        if (mBuilt) throw new IllegalStateException("build() method was already called.");
        mBuilt = true;
        return new InteractionTurn(mName, mPrompts, mInteractionRecognition, mInteractionRecording);
    }

}