/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;
import static java.util.Arrays.*;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.turn.output.grammar.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * An <code>Interaction</code> is a {@link VoiceXmlOutputTurn} that
 * represents a list of {@link Interaction.Prompt} with an optional final
 * recognition or recording phase.
 * <p>
 * Each {@link Interaction.Prompt} represents a phase of the interaction with a
 * sequence of {@link AudioItem} and optional speech/dtmf recognition
 * configurations.
 * 
 * @author Nu Echo Inc.
 * @see Interaction.Prompt
 * @see Interaction.FinalRecognitionWindow
 * @see Interaction.FinalRecordingWindow
 */
public class Interaction extends VoiceXmlOutputTurn {
    private static final String INTERACTION_TURN_TYPE = "interaction";

    private static final String RECORDING_PROPERTY = "recording";
    private static final String RECOGNITION_PROPERTY = "recognition";
    private static final String PROMPTS_PROPERTY = "prompts";

    private final List<Prompt> mPrompts;
    private final FinalRecognitionWindow mFinalRecognitionWindow;
    private final FinalRecordingWindow mFinalRecordingWindow;

    /**
     * @param name The name of this turn. Not empty.
     * @param prompts The list of {@link Prompt}. Not null.
     */
    Interaction(String name, List<Prompt> prompts) {
        super(name);
        Assert.notNull(prompts, "prompts");
        mPrompts = new ArrayList<Prompt>(prompts);
        mFinalRecognitionWindow = null;
        mFinalRecordingWindow = null;
    }

    /**
     * @param name The name of this turn. Not empty.
     * @param prompts The list of {@link Prompt}. Not null.
     * @param finalRecognitionWindow The final recognition phase configuration.
     *            Not null.
     */
    Interaction(String name, List<Prompt> prompts, FinalRecognitionWindow finalRecognitionWindow) {
        super(name);
        Assert.notNull(prompts, "prompts");
        Assert.notNull(finalRecognitionWindow, "recognition");
        mPrompts = new ArrayList<Prompt>(prompts);
        mFinalRecognitionWindow = finalRecognitionWindow;
        mFinalRecordingWindow = null;
    }

    /**
     * @param name The name of this turn. Not empty.
     * @param prompts The list of {@link Prompt}. Not null.
     * @param finalRecordingWindow The final recording phase configuration. Not
     *            null.
     */
    Interaction(String name, List<Prompt> prompts, FinalRecordingWindow finalRecordingWindow) {
        super(name);
        Assert.notNull(prompts, "prompts");
        Assert.notNull(finalRecordingWindow, "recording");
        mPrompts = new ArrayList<Prompt>(prompts);
        mFinalRecordingWindow = finalRecordingWindow;
        mFinalRecognitionWindow = null;
    }

    protected final List<Prompt> getPrompts() {
        return Collections.unmodifiableList(mPrompts);
    }

    protected final FinalRecognitionWindow getRecognition() {
        return mFinalRecognitionWindow;
    }

    protected final FinalRecordingWindow getRecording() {
        return mFinalRecordingWindow;
    }

    @Override
    protected final String getOuputTurnType() {
        return INTERACTION_TURN_TYPE;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, PROMPTS_PROPERTY, JsonUtils.toJson(mPrompts));
        JsonUtils.add(builder, RECOGNITION_PROPERTY, mFinalRecognitionWindow);
        JsonUtils.add(builder, RECORDING_PROPERTY, mFinalRecordingWindow);
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {

        DtmfRecognition dtmfGlobalRecognition = factorizeGlobalDtmfRecognition();
        SpeechRecognition speechGlobalRecognition = factorizeGlobalSpeechRecognition();

        processDtmfRecognition(dtmfGlobalRecognition, formElement);
        processSpeechRecognition(speechGlobalRecognition, formElement);

        boolean hasAtLeastOneField = false;

        Element formItemElement = null;
        boolean recognitionMergedWithLastPrompt = false;
        for (int interactionPromptIndex = 0; interactionPromptIndex < mPrompts.size(); interactionPromptIndex++) {
            Prompt prompt = mPrompts.get(interactionPromptIndex);
            DtmfRecognition dtmfRecognition = prompt.getDtmfRecognition();
            SpeechRecognition speechRecognition = prompt.getSpeechRecognition();

            boolean usingField;
            boolean bargeIn = prompt.getDtmfRecognition() != null || prompt.getSpeechRecognition() != null;

            if (dtmfRecognition == null && speechRecognition == null) {
                formItemElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
                usingField = false;
            } else {
                formItemElement = DomUtils.appendNewElement(formElement, FIELD_ELEMENT);
                addBargeIn(prompt, dtmfRecognition, speechRecognition, formItemElement);
                usingField = true;
            }

            hasAtLeastOneField |= usingField;

            String formItemName = PROMPT_FORM_ITEM_NAME_PREFIX + interactionPromptIndex;
            formItemElement.setAttribute(NAME_ATTRIBUTE, formItemName);

            processDtmfRecognition(getLocalDtmfRecognition(dtmfRecognition, dtmfGlobalRecognition), formItemElement);

            processSpeechRecognition(getLocalSpeechRecognition(speechRecognition, speechGlobalRecognition),
                                     formItemElement);

            if (usingField) {
                if (mFinalRecognitionWindow != null
                    && interactionPromptIndex == mPrompts.size() - 1
                    && same(mFinalRecognitionWindow.getDtmfRecognition(), prompt.getDtmfRecognition())
                    && same(mFinalRecognitionWindow.getSpeechRecognition(), prompt.getSpeechRecognition())) {
                    addTimeProperty(formItemElement, TIMEOUT_PROPERTY, mFinalRecognitionWindow.getNoInputTimeout());
                    recognitionMergedWithLastPrompt = true;
                } else {
                    addTimeProperty(formItemElement, TIMEOUT_PROPERTY, TimeValue.ZERO);
                }
            }

            renderPrompts(prompt, formItemElement, dialogueContext, bargeIn);

            if (usingField && !recognitionMergedWithLastPrompt) {
                Element noInputElement = DomUtils.appendNewElement(formItemElement, NOINPUT_ELEMENT);
                createAssignation(noInputElement, formItemName, TRUE);
                DomUtils.appendNewElement(noInputElement, REPROMPT_ELEMENT);
            }
        }

        if (mFinalRecognitionWindow != null) {
            if (!recognitionMergedWithLastPrompt) {
                hasAtLeastOneField = true;
                Element recognitionFormItemElement = DomUtils.appendNewElement(formElement, FIELD_ELEMENT);
                recognitionFormItemElement.setAttribute(NAME_ATTRIBUTE, RECOGNITION_FORM_ITEM_NAME);
                processDtmfRecognition(getLocalDtmfRecognition(mFinalRecognitionWindow.getDtmfRecognition(),
                                                               dtmfGlobalRecognition),
                                       recognitionFormItemElement);

                processSpeechRecognition(getLocalSpeechRecognition(mFinalRecognitionWindow.getSpeechRecognition(),
                                                                   speechGlobalRecognition),
                                         recognitionFormItemElement);

                addTimeProperty(recognitionFormItemElement,
                                TIMEOUT_PROPERTY,
                                mFinalRecognitionWindow.getNoInputTimeout());
            }
        } else if (mFinalRecordingWindow != null) {
            Element recordingFormItemElement = DomUtils.appendNewElement(formElement, RECORD_ELEMENT);
            recordingFormItemElement.setAttribute(NAME_ATTRIBUTE, RECORD_FORM_ITEM_NAME);

            Recording recording = mFinalRecordingWindow.getRecording();
            DtmfRecognition dtmfTermRecognition = recording.getDtmfTermRecognition();
            if (dtmfTermRecognition != null) {
                processDtmfRecognition(dtmfTermRecognition, recordingFormItemElement);
            }

            setBooleanAttribute(recordingFormItemElement, BEEP_ATTRIBUTE, recording.getBeep());
            setBooleanAttribute(recordingFormItemElement, DTMFTERM_ATTRIBUTE, recording.getDtmfTerm());

            addTimeProperty(recordingFormItemElement, TIMEOUT_PROPERTY, mFinalRecordingWindow.getNoInputTimeout());
            setTimeAttribute(recordingFormItemElement, FINAL_SILENCE_ATTRIBUTE, recording.getFinalSilence());
            setTimeAttribute(recordingFormItemElement, MAXTIME_ATTRIBUTE, recording.getMaximumTime());
            setAttribute(recordingFormItemElement, TYPE_ATTRIBUTE, recording.getType());

            Element filledElement = DomUtils.appendNewElement(recordingFormItemElement, FILLED_ELEMENT);

            List<? extends AudioItem> acknowledgeAudioItems = mFinalRecordingWindow.getAcknowledgeAudioItems();
            if (!acknowledgeAudioItems.isEmpty()) {
                createPrompt(null, recordingFormItemElement, dialogueContext, false, acknowledgeAudioItems);
            }

            String clientSideAssignationDestination = recording.getClientSideAssignationDestination();
            if (clientSideAssignationDestination != null) {
                createAssignation(filledElement, clientSideAssignationDestination, RECORD_FORM_ITEM_NAME);
            }

            createScript(filledElement, RIVR_SCOPE_OBJECT
                                        + ".addRecordingResult(dialog."
                                        + RECORD_FORM_ITEM_NAME
                                        + ", dialog."
                                        + RECORD_FORM_ITEM_NAME
                                        + "$, "
                                        + recording.isPostAudioToServer()
                                        + ");");
            createGotoSubmit(filledElement);
        } else {
            if (hasAtLeastOneField) {
                Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
                createGotoSubmit(blockElement);
            } else {
                createGotoSubmit(formItemElement);
            }
        }

        if (hasAtLeastOneField) {
            createFormLevelFilled(formElement);
        }
    }

    private static void renderPrompts(Prompt prompt,
                                      Element parentElement,
                                      VoiceXmlDialogueContext voiceXmlDialogueContext,
                                      boolean bargeIn) throws VoiceXmlDocumentRenderingException {
        List<? extends AudioItem> audioItems = prompt.getAudioItems();
        createPrompt(prompt.getLanguage(), parentElement, voiceXmlDialogueContext, bargeIn, audioItems);
    }

    private SpeechRecognition factorizeGlobalSpeechRecognition() {

        List<SpeechRecognition> speechConfigurations = getSpeechRecognitions();
        if (speechConfigurations.isEmpty()) return null;

        SpeechRecognition speechGlobalRecognition = speechConfigurations.get(0).copy();
        // unfortunately, we cannot use form-level grammars because of VoiceXML
        // semantic mapping (section 3.1.6)
        speechGlobalRecognition.setGrammarItems(new GrammarItem[0]);

        for (SpeechRecognition speechConfiguration : speechConfigurations) {
            SpeechRecognition configuration = speechConfiguration;
            Assert.notNull(configuration, "configuration");

            if (!same(configuration.getCompleteTimeout(), speechGlobalRecognition.getCompleteTimeout())) {
                speechGlobalRecognition.setCompleteTimeout(null);
            }

            if (!same(configuration.getIncompleteTimeout(), speechGlobalRecognition.getIncompleteTimeout())) {
                speechGlobalRecognition.setIncompleteTimeout(null);
            }

            if (!same(configuration.getConfidenceLevel(), speechGlobalRecognition.getConfidenceLevel())) {
                speechGlobalRecognition.setConfidenceLevel(null);
            }

            if (!same(configuration.getMaxSpeechTimeout(), speechGlobalRecognition.getMaxSpeechTimeout())) {
                speechGlobalRecognition.setMaxSpeechTimeout(null);
            }

            if (!same(configuration.getSensitivity(), speechGlobalRecognition.getSensitivity())) {
                speechGlobalRecognition.setSensitivity(null);
            }

            if (!same(configuration.getSpeedVersusAccuracy(), speechGlobalRecognition.getSpeedVersusAccuracy())) {
                speechGlobalRecognition.setSpeedVersusAccuracy(null);
            }

            if (!same(configuration.getMaxNBest(), speechGlobalRecognition.getMaxNBest())) {
                speechGlobalRecognition.setMaxNBest(null);
            }

            factorizeParameters(speechGlobalRecognition, configuration);
        }

        return speechGlobalRecognition;
    }

    private DtmfRecognition factorizeGlobalDtmfRecognition() {
        List<DtmfRecognition> dtmfRecognitions = getDtmfRecognitions();
        if (dtmfRecognitions.isEmpty()) return null;

        DtmfRecognition dtmfGlobalRecognition = dtmfRecognitions.get(0).copy();
        // unfortunately, we cannot use form-level grammars because of VoiceXML
        // semantic mapping (section 3.1.6)
        dtmfGlobalRecognition.setGrammarItems(new GrammarItem[0]);

        for (DtmfRecognition dtmfRecognition : dtmfRecognitions) {
            DtmfRecognition configuration = dtmfRecognition;
            Assert.notNull(configuration, "configuration");

            if (!same(configuration.getInterDigitTimeout(), dtmfGlobalRecognition.getInterDigitTimeout())) {
                dtmfGlobalRecognition.setInterDigitTimeout(null);
            }

            if (!same(configuration.getTermTimeout(), dtmfGlobalRecognition.getTermTimeout())) {
                dtmfGlobalRecognition.setTermTimeout(null);
            }

            if (!same(configuration.getTermChar(), dtmfGlobalRecognition.getTermChar())) {
                dtmfGlobalRecognition.setTermChar(null);
            }

            factorizeParameters(dtmfGlobalRecognition, configuration);
        }

        return dtmfGlobalRecognition;
    }

    private List<SpeechRecognition> getSpeechRecognitions() {

        List<SpeechRecognition> configurations = new ArrayList<SpeechRecognition>();

        for (Prompt prompt : mPrompts) {
            SpeechRecognition speechRecognition = prompt.getSpeechRecognition();
            if (speechRecognition != null) {
                configurations.add(speechRecognition);
            }

        }

        if (mFinalRecognitionWindow != null) {
            SpeechRecognition speechRecognition = mFinalRecognitionWindow.getSpeechRecognition();
            if (speechRecognition != null) {
                configurations.add(speechRecognition);
            }
        }

        return configurations;
    }

    private List<DtmfRecognition> getDtmfRecognitions() {
        List<DtmfRecognition> configurations = new ArrayList<DtmfRecognition>();

        for (Prompt prompt : getPrompts()) {
            DtmfRecognition dtmfRecognition = prompt.getDtmfRecognition();
            if (dtmfRecognition != null) {
                configurations.add(dtmfRecognition);
            }

        }

        if (mFinalRecognitionWindow != null) {
            DtmfRecognition dtmfRecognition = mFinalRecognitionWindow.getDtmfRecognition();
            if (dtmfRecognition != null) {
                configurations.add(dtmfRecognition);
            }
        }

        return configurations;
    }

    private static boolean same(Object object1, Object object2) {
        if (object1 == null) return object2 == null;
        if (object2 == null) return false;
        return object1.equals(object2);
    }

    private static void factorizeParameters(Recognition globalRecognition, Recognition localRecognition) {
        for (String propertyName : globalRecognition.getPropertyNames()) {

            if (!same(localRecognition.getProperty(propertyName), globalRecognition.getProperty(propertyName))) {
                globalRecognition.removeProperty(propertyName);
            }
        }
    }

    private static void addBargeIn(Prompt prompt,
                                   DtmfRecognition dtmfRecognition,
                                   SpeechRecognition speechRecognition,
                                   Element formItemElement) {
        BargeInType bargeInType = prompt.getBargeInType();
        if (bargeInType != null) {
            addProperty(formItemElement, BARGE_IN_TYPE_PROPERTY, bargeInType.name());
        }

        if (dtmfRecognition != null && speechRecognition != null) {
            addProperty(formItemElement, INPUT_MODES_PROPERTY, DTMF_VOICE_INPUT_MODE);
        } else if (dtmfRecognition == null) {
            addProperty(formItemElement, INPUT_MODES_PROPERTY, VOICE_INPUT_MODE);
        } else if (speechRecognition == null) {
            addProperty(formItemElement, INPUT_MODES_PROPERTY, DTMF_INPUT_MODE);
        }
    }

    private static DtmfRecognition getLocalDtmfRecognition(DtmfRecognition dtmfRecognition,
                                                           DtmfRecognition globalDtmfRecognition) {
        if (dtmfRecognition == null) return null;

        DtmfRecognition localDtmfRecognition = dtmfRecognition.copy();

        if (globalDtmfRecognition.getInterDigitTimeout() != null) {
            localDtmfRecognition.setInterDigitTimeout(null);
        }

        if (globalDtmfRecognition.getTermTimeout() != null) {
            localDtmfRecognition.setTermTimeout(null);
        }

        if (globalDtmfRecognition.getTermChar() != null) {
            localDtmfRecognition.setTermChar(null);
        }

        removeGlobalParameters(globalDtmfRecognition, localDtmfRecognition);

        return localDtmfRecognition;
    }

    private static SpeechRecognition getLocalSpeechRecognition(SpeechRecognition speechRecognition,
                                                               SpeechRecognition globalSpeechRecognition) {
        if (speechRecognition == null) return null;

        SpeechRecognition localSpeechRecognition = speechRecognition.copy();

        if (globalSpeechRecognition.getCompleteTimeout() != null) {
            localSpeechRecognition.setCompleteTimeout(null);
        }

        if (globalSpeechRecognition.getIncompleteTimeout() != null) {
            localSpeechRecognition.setIncompleteTimeout(null);
        }

        if (globalSpeechRecognition.getConfidenceLevel() != null) {
            localSpeechRecognition.setConfidenceLevel(null);
        }

        if (globalSpeechRecognition.getIncompleteTimeout() != null) {
            localSpeechRecognition.setIncompleteTimeout(null);
        }

        if (globalSpeechRecognition.getMaxNBest() != null) {
            localSpeechRecognition.setMaxNBest(null);
        }

        if (globalSpeechRecognition.getMaxSpeechTimeout() != null) {
            localSpeechRecognition.setMaxSpeechTimeout(null);
        }

        if (globalSpeechRecognition.getSensitivity() != null) {
            localSpeechRecognition.setSensitivity(null);
        }

        if (globalSpeechRecognition.getSpeedVersusAccuracy() != null) {
            localSpeechRecognition.setSpeedVersusAccuracy(null);
        }

        removeGlobalParameters(globalSpeechRecognition, localSpeechRecognition);

        return localSpeechRecognition;
    }

    private static void removeGlobalParameters(Recognition globalRecognition, Recognition localRecognition) {
        for (String propertyName : globalRecognition.getPropertyNames()) {
            localRecognition.removeProperty(propertyName);
        }
    }

    private void createFormLevelFilled(Element parent) throws VoiceXmlDocumentRenderingException {
        Element filledElement = DomUtils.appendNewElement(parent, FILLED_ELEMENT);
        filledElement.setAttribute(MODE_ATTRIBUTE, ANY_MODE);

        if (mFinalRecognitionWindow != null) {
            List<? extends AudioItem> acknowledgeAudioItems = mFinalRecognitionWindow.getAcknowledgeAudioItems();
            if (!acknowledgeAudioItems.isEmpty()) {
                processAudioItems(acknowledgeAudioItems, filledElement);
            }
        }

        createScript(filledElement, RIVR_SCOPE_OBJECT + ".addRecognitionResult()");
        createGotoSubmit(filledElement);
    }

    public enum BargeInType {
        speech, hotword;
    }

    /**
     * A <code>FinalRecognitionWindow</code> is an optional final phase of an
     * {@link Interaction}.
     * <p>
     * It specifies a recognition configuration, and optionally, a no input
     * timeout
     * and a sequence of {@link AudioItem} that is played if a recognition is
     * successful.
     * 
     * @author Nu Echo Inc.
     */
    static final class FinalRecognitionWindow implements JsonSerializable {

        private static final String NO_INPUT_TIMEOUT_PROPERTY = "noInputTimeout";
        private static final String SPEECH_RECOGNITION_PROPERTY = "speechRecognition";
        private static final String DTMF_RECOGNITION_PROPERTY = "dtmfRecognition";
        private static final String ACKNOWLEDGE_AUDIO_ITEMS_PROPERTY = "acknowledgeAudioItems";

        private final SpeechRecognition mSpeechRecognition;
        private final DtmfRecognition mDtmfRecognition;
        private TimeValue mNoInputTimeout;
        private final List<AudioItem> mAcknowledgeAudioItems;

        public FinalRecognitionWindow(DtmfRecognition dtmfRecognition,
                                      SpeechRecognition speechRecognition,
                                      TimeValue noInputTimeout,
                                      AudioItem... acknowledgeAudioItems) {
            this(dtmfRecognition, speechRecognition, noInputTimeout, asList(acknowledgeAudioItems));
        }

        public FinalRecognitionWindow(DtmfRecognition dtmfRecognition,
                                      SpeechRecognition speechRecognition,
                                      TimeValue noInputTimeout,
                                      List<? extends AudioItem> acknowledgeAudioItems) {
            mSpeechRecognition = speechRecognition;
            mDtmfRecognition = dtmfRecognition;
            mNoInputTimeout = noInputTimeout;
            mAcknowledgeAudioItems = new ArrayList<AudioItem>(acknowledgeAudioItems);
            assertInvariant();
        }

        public SpeechRecognition getSpeechRecognition() {
            return mSpeechRecognition;
        }

        public DtmfRecognition getDtmfRecognition() {
            return mDtmfRecognition;
        }

        public TimeValue getNoInputTimeout() {
            return mNoInputTimeout;
        }

        public List<? extends AudioItem> getAcknowledgeAudioItems() {
            return Collections.unmodifiableList(mAcknowledgeAudioItems);
        }

        public void setNoInputTimeout(TimeValue noInputTimeout) {
            mNoInputTimeout = noInputTimeout;
        }

        private void assertInvariant() {
            Assert.ensure(mSpeechRecognition != null || mDtmfRecognition != null,
                          "Must provide a non-null configuration for speech or DTMF");
        }

        @Override
        public JsonValue asJson() {
            JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
            JsonUtils.add(builder, ACKNOWLEDGE_AUDIO_ITEMS_PROPERTY, JsonUtils.toJson(mAcknowledgeAudioItems));
            JsonUtils.add(builder, DTMF_RECOGNITION_PROPERTY, mDtmfRecognition);
            JsonUtils.add(builder, SPEECH_RECOGNITION_PROPERTY, mSpeechRecognition);
            JsonUtils.addTimeProperty(builder, NO_INPUT_TIMEOUT_PROPERTY, mNoInputTimeout);
            return builder.build();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mAcknowledgeAudioItems == null) ? 0 : mAcknowledgeAudioItems.hashCode());
            result = prime * result + ((mDtmfRecognition == null) ? 0 : mDtmfRecognition.hashCode());
            result = prime * result + ((mNoInputTimeout == null) ? 0 : mNoInputTimeout.hashCode());
            result = prime * result + ((mSpeechRecognition == null) ? 0 : mSpeechRecognition.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            FinalRecognitionWindow other = (FinalRecognitionWindow) obj;
            if (mAcknowledgeAudioItems == null) {
                if (other.mAcknowledgeAudioItems != null) return false;
            } else if (!mAcknowledgeAudioItems.equals(other.mAcknowledgeAudioItems)) return false;
            if (mDtmfRecognition == null) {
                if (other.mDtmfRecognition != null) return false;
            } else if (!mDtmfRecognition.equals(other.mDtmfRecognition)) return false;
            if (mNoInputTimeout == null) {
                if (other.mNoInputTimeout != null) return false;
            } else if (!mNoInputTimeout.equals(other.mNoInputTimeout)) return false;
            if (mSpeechRecognition == null) {
                if (other.mSpeechRecognition != null) return false;
            } else if (!mSpeechRecognition.equals(other.mSpeechRecognition)) return false;
            return true;
        }

        @Override
        public String toString() {
            return asJson().toString();
        }
    }

    /**
     * A <code>FinalRecordingWindow</code> is an optional final phase of an
     * {@link Interaction}.
     * <p>
     * It specifies a recording configuration, and optionally, a no input
     * timeout
     * and a sequence of {@link AudioItem} that is played if a recording is
     * successful.
     * 
     * @author Nu Echo Inc.
     */
    static final class FinalRecordingWindow implements JsonSerializable {
        private static final String ACKNOWLEDGE_AUDIO_ITEMS_PROPERTY = "acknowledgeAudioItems";
        @SuppressWarnings("hiding")
        private static final String RECORDING_PROPERTY = "recording";
        private static final String NO_INPUT_TIMEOUT_PROPERTY = "noInputTimeout";

        private final Recording mRecording;
        private final TimeValue mNoInputTimeout;
        private final List<AudioItem> mAcknowledgeAudioItems;

        public FinalRecordingWindow(Recording recording, TimeValue noInputTimeout, AudioItem... acknowledgeAudioItems) {
            this(recording, noInputTimeout, asList(acknowledgeAudioItems));
        }

        public FinalRecordingWindow(Recording recording,
                                    TimeValue noInputTimeout,
                                    List<? extends AudioItem> acknowledgeAudioItems) {
            Assert.notNull(recording, "recording");
            Assert.notNull(acknowledgeAudioItems, "acknowledgeAudioItems");
            mRecording = recording;
            mNoInputTimeout = noInputTimeout;
            mAcknowledgeAudioItems = new ArrayList<AudioItem>(acknowledgeAudioItems);
        }

        public Recording getRecording() {
            return mRecording;
        }

        public TimeValue getNoInputTimeout() {
            return mNoInputTimeout;
        }

        public List<? extends AudioItem> getAcknowledgeAudioItems() {
            return mAcknowledgeAudioItems;
        }

        @Override
        public JsonValue asJson() {
            JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
            JsonUtils.addTimeProperty(builder, NO_INPUT_TIMEOUT_PROPERTY, mNoInputTimeout);
            JsonUtils.add(builder, RECORDING_PROPERTY, mRecording);
            JsonUtils.add(builder, ACKNOWLEDGE_AUDIO_ITEMS_PROPERTY, JsonUtils.toJson(mAcknowledgeAudioItems));
            return builder.build();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mAcknowledgeAudioItems == null) ? 0 : mAcknowledgeAudioItems.hashCode());
            result = prime * result + ((mNoInputTimeout == null) ? 0 : mNoInputTimeout.hashCode());
            result = prime * result + ((mRecording == null) ? 0 : mRecording.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            FinalRecordingWindow other = (FinalRecordingWindow) obj;
            if (mAcknowledgeAudioItems == null) {
                if (other.mAcknowledgeAudioItems != null) return false;
            } else if (!mAcknowledgeAudioItems.equals(other.mAcknowledgeAudioItems)) return false;
            if (mNoInputTimeout == null) {
                if (other.mNoInputTimeout != null) return false;
            } else if (!mNoInputTimeout.equals(other.mNoInputTimeout)) return false;
            if (mRecording == null) {
                if (other.mRecording != null) return false;
            } else if (!mRecording.equals(other.mRecording)) return false;
            return true;
        }

        @Override
        public String toString() {
            return asJson().toString();
        }
    }

    /**
     * An <code>Prompt</code> represent a phase in an {@link Interaction} and is
     * composed of a sequence of {@link AudioItem} and optionally a speech
     * and/or a dtmf recognition configuration.
     * 
     * @author Nu Echo Inc.
     */
    static final class Prompt implements JsonSerializable {
        private static final String SPEECH_RECOGNITION_PROPERTY = "speechRecognition";
        private static final String DTMF_RECOGNITION_PROPERTY = "dtmfRecognition";
        @SuppressWarnings("hiding")
        private static final String BARGE_IN_TYPE_PROPERTY = "bargeInType";
        private static final String AUDIO_ITEMS_PROPERTY = "audioItems";
        private static final String LANGUAGE_PROPERTY = "language";

        private final List<AudioItem> mAudioItems;
        private final SpeechRecognition mSpeechRecognition;
        private final DtmfRecognition mDtmfRecognition;

        private String mLanguage;
        private BargeInType mBargeInType;

        /**
         * @param speechRecognition The speech recognition
         *            configuration. Optional.
         * @param dtmfRecognition The DTMF recognition
         *            configuration.
         *            Optional.
         * @param audioItems The list of {@link AudioItem}. Not null.
         */
        public Prompt(SpeechRecognition speechRecognition,
                      DtmfRecognition dtmfRecognition,
                      List<? extends AudioItem> audioItems) {
            Assert.notNull(audioItems, "audioItems");
            mAudioItems = new ArrayList<AudioItem>(audioItems);
            mSpeechRecognition = speechRecognition;
            mDtmfRecognition = dtmfRecognition;
        }

        /**
         * @param speechRecognition The speech recognition
         *            configuration.
         * @param dtmfRecognition The DTMF recognition
         *            configuration.
         * @param audioItems The list of {@link AudioItem}. Not null.
         */
        public Prompt(SpeechRecognition speechRecognition, DtmfRecognition dtmfRecognition, AudioItem... audioItems) {
            this(speechRecognition, dtmfRecognition, asList(audioItems));
        }

        /**
         * @param speechRecognition The speech recognition
         *            configuration.
         * @param audioItems The list of {@link AudioItem}. Not null.
         */
        public Prompt(SpeechRecognition speechRecognition, List<? extends AudioItem> audioItems) {
            this(speechRecognition, null, audioItems);
        }

        /**
         * @param speechRecognition The speech recognition
         *            configuration.
         * @param audioItems The list of {@link AudioItem}. Not null.
         */
        public Prompt(SpeechRecognition speechRecognition, AudioItem... audioItems) {
            this(speechRecognition, null, audioItems);
        }

        /**
         * @param dtmfRecognition The DTMF recognition
         *            configuration.
         * @param audioItems The list of {@link AudioItem}. Not null.
         */
        public Prompt(DtmfRecognition dtmfRecognition, List<? extends AudioItem> audioItems) {
            this(null, dtmfRecognition, audioItems);
        }

        /**
         * @param dtmfRecognition The DTMF recognition
         *            configuration.
         * @param audioItems The list of {@link AudioItem}. Not null.
         */
        public Prompt(DtmfRecognition dtmfRecognition, AudioItem... audioItems) {
            this(null, dtmfRecognition, audioItems);
        }

        /**
         * @param audioItems The list of {@link AudioItem}. Not null.
         */
        public Prompt(List<? extends AudioItem> audioItems) {
            this(null, null, audioItems);
        }

        /**
         * @param audioItems The list of {@link AudioItem}. Not null.
         */
        public Prompt(AudioItem... audioItems) {
            this(null, null, audioItems);
        }

        /**
         * @param language language code for this prompt. <code>null</code> if
         *            language should be reset to platform-specific default
         *            value
         *            for the prompts to be added.
         */
        public void setLanguage(String language) {
            mLanguage = language;
        }

        /**
         * @param bargeInType {@link BargeInType#speech} or
         *            {@link BargeInType#hotword}. <code>null</code> if language
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

        public SpeechRecognition getSpeechRecognition() {
            return mSpeechRecognition;
        }

        public DtmfRecognition getDtmfRecognition() {
            return mDtmfRecognition;
        }

        public BargeInType getBargeInType() {
            return mBargeInType;
        }

        @Override
        public JsonValue asJson() {
            JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
            JsonUtils.add(builder, LANGUAGE_PROPERTY, mLanguage);
            JsonUtils.add(builder, BARGE_IN_TYPE_PROPERTY, mBargeInType == null ? null : mBargeInType.name());
            JsonUtils.add(builder, AUDIO_ITEMS_PROPERTY, JsonUtils.toJson(mAudioItems));
            JsonUtils.add(builder, DTMF_RECOGNITION_PROPERTY, mDtmfRecognition);
            JsonUtils.add(builder, SPEECH_RECOGNITION_PROPERTY, mSpeechRecognition);
            return builder.build();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mAudioItems == null) ? 0 : mAudioItems.hashCode());
            result = prime * result + ((mBargeInType == null) ? 0 : mBargeInType.hashCode());
            result = prime * result + ((mDtmfRecognition == null) ? 0 : mDtmfRecognition.hashCode());
            result = prime * result + ((mLanguage == null) ? 0 : mLanguage.hashCode());
            result = prime * result + ((mSpeechRecognition == null) ? 0 : mSpeechRecognition.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Prompt other = (Prompt) obj;
            if (mAudioItems == null) {
                if (other.mAudioItems != null) return false;
            } else if (!mAudioItems.equals(other.mAudioItems)) return false;
            if (mBargeInType != other.mBargeInType) return false;
            if (mDtmfRecognition == null) {
                if (other.mDtmfRecognition != null) return false;
            } else if (!mDtmfRecognition.equals(other.mDtmfRecognition)) return false;
            if (mLanguage == null) {
                if (other.mLanguage != null) return false;
            } else if (!mLanguage.equals(other.mLanguage)) return false;
            if (mSpeechRecognition == null) {
                if (other.mSpeechRecognition != null) return false;
            } else if (!mSpeechRecognition.equals(other.mSpeechRecognition)) return false;
            return true;
        }

        @Override
        public String toString() {
            return asJson().toString();
        }
    }

    /**
     * Builder used to ease creation of instances of {@link Interaction} <p>
     * Building an {@link Interaction} implies the following steps:
     * <ul>
     * <li>Add some prompts</li>
     * <ul>
     * <li>For each prompt, speech/DTMF recognition can be specified (thus
     * enabling
     * <i>barge-in</i> when the prompt is played).
     * </ul>
     * <li>Once all prompts are added, optionally specify either:
     * <ul>
     * <li>a final recognition window (speech or DTMF)</li>
     * <li>a recording</li>
     * </ul>
     * </ul>
     * <p>
     * At any time, it is possible to change the current language used for
     * prompts
     * (relevant to speech synthesis) and the barge-in type, i.e. <i>speech</i>
     * or
     * <i>hotword</i>.
     * <p>
     * This can be translated to:
     * <pre>
     * Builder builder = Builder.newInteraction();
     * builder.addPrompt(...);
     * builder.addPrompt(...);
     * //repeat as needed
     * builder.addPrompt(...);
     * Interaction interaction = builder.build(...);
     * </pre>
     * 
     * @author Nu Echo Inc.
     */
    public static final class Builder {
        private final String mTurnName;
        private final List<Prompt> mPrompts = new ArrayList<Prompt>();

        private String mLanguage;
        private BargeInType mBargeInType;

        private boolean mBuilt;

        private Builder(String turnName) {
            mTurnName = turnName;
        }

        /**
         * Creates an Builder.
         * 
         * @param turnName of the interaction to be created.
         */

        public static Builder newInteraction(String turnName) {
            return new Builder(turnName);
        }

        /**
         * Sets the barge-in type to either "speech" or "hotword" for the
         * prompts
         * that will be added using one of the <code>addPrompt(...)</code>
         * methods.
         * <p>
         * Note: When an <code>Builder</code> is created, the default
         * value for this flag is <code>null</code>, meaning the barge-in type
         * will
         * be platform-dependant.
         * 
         * @param bargeInType {@link BargeInType#speech} or
         *            {@link BargeInType#hotword}. <code>null</code> reverts to
         *            platform default value.
         */
        public Builder setBargeInType(BargeInType bargeInType) {
            mBargeInType = bargeInType;
            return this;
        }

        /**
         * Sets the language code for the prompts that will be added using one
         * of
         * the <code>addPrompt(...)</code> methods.
         * <p>
         * Note: When an <code>Builder</code> is created, the default
         * value for this property is <code>null</code>, i.e. no explicit
         * language
         * code will be generated in the VoiceXML thus relying on the
         * platform-specific default language code.
         * 
         * @param language language code for this prompt. <code>null</code> if
         *            language should be reset to platform-specific default
         *            value
         *            for the prompts to be added.
         */
        public Builder setLanguage(String language) {
            mLanguage = language;
            return this;
        }

        /**
         * Adds a prompt with DTMF recognition only.
         * 
         * @param dtmfRecognition configuration for the DTMF
         *            recognition
         * @param audioItems audio items to be played during this prompt.
         */
        public Builder addPrompt(DtmfRecognition dtmfRecognition, AudioItem... audioItems) {
            return addPrompt(dtmfRecognition, null, asList(audioItems));
        }

        /**
         * Adds a prompt with DTMF recognition only.
         * 
         * @param dtmfRecognition configuration for the DTMF
         *            recognition
         * @param audioItems audio items to be played during this prompt.
         */
        public Builder addPrompt(DtmfRecognition dtmfRecognition, List<? extends AudioItem> audioItems) {
            return addPrompt(dtmfRecognition, null, audioItems);
        }

        /**
         * Adds a prompt with speech recognition only.
         * 
         * @param speechRecognition configuration for the speech
         *            recognition
         * @param audioItems audio items to be played during this prompt.
         */
        public Builder addPrompt(SpeechRecognition speechRecognition, AudioItem... audioItems) {
            return addPrompt(null, speechRecognition, asList(audioItems));
        }

        /**
         * Adds a prompt with speech recognition only.
         * 
         * @param speechRecognition configuration for the speech
         *            recognition
         * @param audioItems audio items to be played during this prompt.
         */
        public Builder addPrompt(SpeechRecognition speechRecognition, List<? extends AudioItem> audioItems) {
            return addPrompt(null, speechRecognition, audioItems);
        }

        /**
         * Adds a prompt with both DTMF and speech recognition.
         * 
         * @param speechRecognition configuration for the speech
         *            recognition or <code>null</code> to disable DTMF
         *            recognition.
         * @param dtmfRecognition configuration for the DTMF
         *            recognition or <code>null</code> to disable DTMF
         *            recognition.
         * @param audioItems audio items to be played during this prompt.
         */
        public Builder addPrompt(DtmfRecognition dtmfRecognition,
                                 SpeechRecognition speechRecognition,
                                 AudioItem... audioItems) {
            return addPrompt(dtmfRecognition, speechRecognition, asList(audioItems));
        }

        /**
         * Adds a prompt with both DTMF and speech recognition.
         * 
         * @param speechRecognition configuration for the speech
         *            recognition or <code>null</code> to disable DTMF
         *            recognition.
         * @param dtmfRecognition configuration for the DTMF
         *            recognition or <code>null</code> to disable DTMF
         *            recognition.
         * @param audioItems audio items to be played during this prompt.
         */
        public Builder addPrompt(DtmfRecognition dtmfRecognition,
                                 SpeechRecognition speechRecognition,
                                 List<? extends AudioItem> audioItems) {
            Prompt prompt = new Prompt(speechRecognition, dtmfRecognition, audioItems);
            prompt.setLanguage(mLanguage);
            prompt.setHotWordBargeIn(mBargeInType);
            mPrompts.add(prompt);
            return this;
        }

        /**
         * Adds a prompt without any DTMF nor speech recognition (no barge-in).
         * 
         * @param audioItems audio items to be played during this prompt.
         */
        public Builder addPrompt(AudioItem... audioItems) {
            return addPrompt(Arrays.asList(audioItems));
        }

        /**
         * Adds a prompt without any DTMF nor speech recognition (no barge-in).
         * 
         * @param audioItems audio items to be played during this prompt.
         */
        public Builder addPrompt(List<? extends AudioItem> audioItems) {
            Prompt prompt = new Prompt(audioItems);
            prompt.setLanguage(mLanguage);
            prompt.setHotWordBargeIn(mBargeInType);
            mPrompts.add(prompt);
            return this;
        }

        /**
         * Builds the interaction. This method adds a DTMF recognition window
         * after
         * the prompts.
         * 
         * @param dtmfRecognition configuration for the DTMF
         *            recognition
         * @param noinputTimeout timeout value before a <code>noinput</code> is
         *            generated.
         * @param acknowledgeAudioItems audioItems to be played upon recognition
         */
        public Interaction build(DtmfRecognition dtmfRecognition,
                                 TimeValue noinputTimeout,
                                 AudioItem... acknowledgeAudioItems) {

            return build(dtmfRecognition, null, noinputTimeout, asList(acknowledgeAudioItems));

        }

        /**
         * Builds the interaction. This method adds a speech recognition window
         * after the prompts.
         * 
         * @param speechRecognition configuration for the speech
         *            recognition
         * @param noinputTimeout timeout value before a <code>noinput</code> is
         *            generated.
         * @param acknowledgeAudioItems audioItems to be played upon recognition
         */
        public Interaction build(SpeechRecognition speechRecognition,
                                 TimeValue noinputTimeout,
                                 AudioItem... acknowledgeAudioItems) {

            return build(null, speechRecognition, noinputTimeout, asList(acknowledgeAudioItems));

        }

        /**
         * Builds the interaction. This method adds a DTMF recognition window
         * after
         * the prompts.
         * 
         * @param dtmfRecognition configuration for the DTMF
         *            recognition
         * @param noinputTimeout timeout value before a <code>noinput</code> is
         *            generated.
         * @param acknowledgeAudioItems audioItems to be played upon recognition
         */
        public Interaction build(DtmfRecognition dtmfRecognition,
                                 TimeValue noinputTimeout,
                                 List<? extends AudioItem> acknowledgeAudioItems) {
            return build(dtmfRecognition, null, noinputTimeout, acknowledgeAudioItems);

        }

        /**
         * Builds the interaction. This method adds a speech recognition window
         * after the prompts.
         * 
         * @param speechRecognition configuration for the speech
         *            recognition
         * @param noinputTimeout timeout value before a <code>noinput</code> is
         *            generated.
         * @param acknowledgeAudioItems audioItems to be played upon recognition
         */
        public Interaction build(SpeechRecognition speechRecognition,
                                 TimeValue noinputTimeout,
                                 List<? extends AudioItem> acknowledgeAudioItems) {
            return build(null, speechRecognition, noinputTimeout, acknowledgeAudioItems);

        }

        /**
         * Builds the interaction. This method adds a speech and DTMF
         * recognition
         * window after the prompts.
         * 
         * @param speechRecognition configuration for the speech
         *            recognition
         * @param dtmfRecognition configuration for the DTMF
         *            recognition
         * @param noinputTimeout timeout value before a <code>noinput</code> is
         *            generated.
         * @param acknowledgeAudioItems audioItems to be played upon recognition
         */
        public Interaction build(DtmfRecognition dtmfRecognition,
                                 SpeechRecognition speechRecognition,
                                 TimeValue noinputTimeout,
                                 AudioItem... acknowledgeAudioItems) {

            return build(dtmfRecognition, speechRecognition, noinputTimeout, asList(acknowledgeAudioItems));

        }

        /**
         * Builds the interaction. This method adds a speech and DTMF
         * recognition
         * window after the prompts.
         * 
         * @param speechRecognition configuration for the speech
         *            recognition
         * @param dtmfRecognition configuration for the DTMF
         *            recognition
         * @param noinputTimeout timeout value before a <code>noinput</code> is
         *            generated.
         * @param acknowledgeAudioItems audioItems to be played upon recognition
         */
        public Interaction build(DtmfRecognition dtmfRecognition,
                                 SpeechRecognition speechRecognition,
                                 TimeValue noinputTimeout,
                                 List<? extends AudioItem> acknowledgeAudioItems) {

            Assert.ensure(dtmfRecognition != null || speechRecognition != null,
                          "Must provide at least one recognition configuration (speech or DTMF)");

            checkBuilt();
            FinalRecognitionWindow finalRecognitionWindow = new FinalRecognitionWindow(dtmfRecognition,
                                                                                       speechRecognition,
                                                                                       noinputTimeout,
                                                                                       acknowledgeAudioItems);
            return new Interaction(mTurnName, mPrompts, finalRecognitionWindow);

        }

        /**
         * Builds the interaction. This method adds a recording window after the
         * prompts.
         * 
         * @param recording configuration for the recording
         * @param noinputTimeout timeout value before a <code>noinput</code> is
         *            generated.
         * @param acknowledgeAudioItems audioItems to be played upon recording
         *            completion
         */
        public Interaction build(Recording recording, TimeValue noinputTimeout, AudioItem... acknowledgeAudioItems) {
            return build(recording, noinputTimeout, asList(acknowledgeAudioItems));
        }

        /**
         * Builds the interaction. This method adds a recording window after the
         * prompts.
         * 
         * @param recording configuration for the recording
         * @param noinputTimeout timeout value before a <code>noinput</code> is
         *            generated.
         * @param acknowledgeAudioItems audioItems to be played upon recording
         *            completion
         */
        public Interaction build(Recording recording,
                                 TimeValue noinputTimeout,
                                 List<? extends AudioItem> acknowledgeAudioItems) {
            Assert.notNull(recording, "recording");
            Assert.notNull(acknowledgeAudioItems, "acknowledgeAudioItems");

            checkBuilt();
            FinalRecordingWindow finalRecordingWindow = new FinalRecordingWindow(recording,
                                                                                 noinputTimeout,
                                                                                 acknowledgeAudioItems);
            return new Interaction(mTurnName, mPrompts, finalRecordingWindow);
        }

        /**
         * Builds the interaction. This method does not allow any recording nor
         * recognition after the prompts.
         */
        public Interaction build() {
            checkBuilt();
            return new Interaction(mTurnName, mPrompts);
        }

        private void checkBuilt() {
            if (mBuilt) throw new IllegalStateException("build() method was already called.");
            mBuilt = true;
        }
    }
}