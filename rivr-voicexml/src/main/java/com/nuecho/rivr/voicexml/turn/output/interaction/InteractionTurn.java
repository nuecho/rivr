/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.interaction;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.turn.output.grammar.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * An <code>InteractionTurn</code> is a {@link VoiceXmlOutputTurn} that
 * represents a list of {@link InteractionPrompt} with an optional final
 * recognition or recording phase.
 * <p>
 * Each {@link InteractionPrompt} represents a phase of the interaction with a
 * sequence of {@link AudioItem} and optional speech/dtmf recognition
 * configurations.
 * 
 * @author Nu Echo Inc.
 * @see InteractionPrompt
 * @see InteractionRecognition
 * @see InteractionRecording
 */
public class InteractionTurn extends VoiceXmlOutputTurn {
    private static final String INTERACTION_TURN_TYPE = "interaction";

    private static final String RECORDING_PROPERTY = "recording";
    private static final String RECOGNITION_PROPERTY = "recognition";
    private static final String PROMPTS_PROPERTY = "prompts";

    private final List<InteractionPrompt> mPrompts;
    private final InteractionRecognition mRecognition;
    private final InteractionRecording mRecording;

    /**
     * @param name The name of this turn. Not empty.
     * @param prompts The list of {@link InteractionPrompt}. Not null.
     */
    public InteractionTurn(String name, List<InteractionPrompt> prompts) {
        super(name);
        Assert.notNull(prompts, "prompts");
        mPrompts = new ArrayList<InteractionPrompt>(prompts);
        mRecognition = null;
        mRecording = null;
    }

    /**
     * @param name The name of this turn. Not empty.
     * @param prompts The list of {@link InteractionPrompt}. Not null.
     * @param recognition The final recognition phase configuration. Not null.
     */
    public InteractionTurn(String name, List<InteractionPrompt> prompts, InteractionRecognition recognition) {
        super(name);
        Assert.notNull(prompts, "prompts");
        Assert.notNull(recognition, "recognition");
        mPrompts = new ArrayList<InteractionPrompt>(prompts);
        mRecognition = recognition;
        mRecording = null;
    }

    /**
     * @param name The name of this turn. Not empty.
     * @param prompts The list of {@link InteractionPrompt}. Not null.
     * @param recording The final recording phase configuration. Not null.
     */
    public InteractionTurn(String name, List<InteractionPrompt> prompts, InteractionRecording recording) {
        super(name);
        Assert.notNull(prompts, "prompts");
        Assert.notNull(recording, "recording");
        mPrompts = new ArrayList<InteractionPrompt>(prompts);
        mRecording = recording;
        mRecognition = null;
    }

    public final List<InteractionPrompt> getPrompts() {
        return Collections.unmodifiableList(mPrompts);
    }

    public final InteractionRecognition getRecognition() {
        return mRecognition;
    }

    public final InteractionRecording getRecording() {
        return mRecording;
    }

    @Override
    protected final String getOuputTurnType() {
        return INTERACTION_TURN_TYPE;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, PROMPTS_PROPERTY, JsonUtils.toJson(mPrompts));
        JsonUtils.add(builder, RECOGNITION_PROPERTY, mRecognition);
        JsonUtils.add(builder, RECORDING_PROPERTY, mRecording);
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {

        DtmfRecognitionConfiguration dtmfGlobalConfiguration = factorizeGlobalDtmfConfiguration();
        SpeechRecognitionConfiguration speechGlobalConfiguration = factorizeGlobalSpeechConfiguration();

        processDtmfConfiguration(dtmfGlobalConfiguration, formElement);
        processSpeechConfiguration(speechGlobalConfiguration, formElement);

        boolean hasAtLeastOneField = false;

        Element formItemElement = null;
        boolean recognitionMergedWithLastPrompt = false;
        for (int interactionPromptIndex = 0; interactionPromptIndex < mPrompts.size(); interactionPromptIndex++) {
            InteractionPrompt prompt = mPrompts.get(interactionPromptIndex);
            DtmfRecognitionConfiguration dtmfRecognitionConfiguration = prompt.getDtmfRecognitionConfiguration();
            SpeechRecognitionConfiguration speechRecognitionConfiguration = prompt.getSpeechRecognitionConfiguration();

            boolean usingField;
            boolean bargeIn = prompt.getDtmfRecognitionConfiguration() != null
                              || prompt.getSpeechRecognitionConfiguration() != null;

            if (dtmfRecognitionConfiguration == null && speechRecognitionConfiguration == null) {
                formItemElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
                usingField = false;
            } else {
                formItemElement = DomUtils.appendNewElement(formElement, FIELD_ELEMENT);
                addBargeIn(prompt, dtmfRecognitionConfiguration, speechRecognitionConfiguration, formItemElement);
                usingField = true;
            }

            hasAtLeastOneField |= usingField;

            String formItemName = PROMPT_FORM_ITEM_NAME_PREFIX + interactionPromptIndex;
            formItemElement.setAttribute(NAME_ATTRIBUTE, formItemName);

            processDtmfConfiguration(getLocalConfiguration(dtmfRecognitionConfiguration, dtmfGlobalConfiguration),
                                     formItemElement);

            processSpeechConfiguration(getLocalConfiguration(speechRecognitionConfiguration, speechGlobalConfiguration),
                                       formItemElement);

            if (usingField) {
                if (mRecognition != null
                    && interactionPromptIndex == mPrompts.size() - 1
                    && same(mRecognition.getDtmfRecognitionConfiguration(), prompt.getDtmfRecognitionConfiguration())
                    && same(mRecognition.getSpeechRecognitionConfiguration(),
                            prompt.getSpeechRecognitionConfiguration())) {
                    addTimeProperty(formItemElement, TIMEOUT_PROPERTY, mRecognition.getNoInputTimeout());
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

        if (mRecognition != null) {
            if (!recognitionMergedWithLastPrompt) {
                hasAtLeastOneField = true;
                Element recognitionFormItemElement = DomUtils.appendNewElement(formElement, FIELD_ELEMENT);
                recognitionFormItemElement.setAttribute(NAME_ATTRIBUTE, RECOGNITION_FORM_ITEM_NAME);
                processDtmfConfiguration(getLocalConfiguration(mRecognition.getDtmfRecognitionConfiguration(),
                                                               dtmfGlobalConfiguration),
                                         recognitionFormItemElement);

                processSpeechConfiguration(getLocalConfiguration(mRecognition.getSpeechRecognitionConfiguration(),
                                                                 speechGlobalConfiguration),
                                           recognitionFormItemElement);

                addTimeProperty(recognitionFormItemElement, TIMEOUT_PROPERTY, mRecognition.getNoInputTimeout());
            }
        } else if (mRecording != null) {
            Element recordingFormItemElement = DomUtils.appendNewElement(formElement, RECORD_ELEMENT);
            recordingFormItemElement.setAttribute(NAME_ATTRIBUTE, RECORD_FORM_ITEM_NAME);

            RecordingConfiguration recordingConfiguration = mRecording.getRecordingConfiguration();
            DtmfRecognitionConfiguration dtmfTermRecognitionConfiguration = recordingConfiguration.getDtmfTermRecognitionConfiguration();
            if (dtmfTermRecognitionConfiguration != null) {
                processDtmfConfiguration(dtmfTermRecognitionConfiguration, recordingFormItemElement);
            }

            setBooleanAttribute(recordingFormItemElement, BEEP_ATTRIBUTE, recordingConfiguration.getBeep());
            setBooleanAttribute(recordingFormItemElement, DTMFTERM_ATTRIBUTE, recordingConfiguration.getDtmfTerm());

            addTimeProperty(recordingFormItemElement, TIMEOUT_PROPERTY, mRecording.getNoInputTimeout());
            setTimeAttribute(recordingFormItemElement,
                             FINAL_SILENCE_ATTRIBUTE,
                             recordingConfiguration.getFinalSilence());
            setTimeAttribute(recordingFormItemElement, MAXTIME_ATTRIBUTE, recordingConfiguration.getMaximumTime());
            setAttribute(recordingFormItemElement, TYPE_ATTRIBUTE, recordingConfiguration.getType());

            Element filledElement = DomUtils.appendNewElement(recordingFormItemElement, FILLED_ELEMENT);

            List<? extends AudioItem> acknowledgeAudioItems = mRecording.getAcknowledgeAudioItems();
            if (!acknowledgeAudioItems.isEmpty()) {
                createPrompt(null, recordingFormItemElement, dialogueContext, false, acknowledgeAudioItems);
            }

            String clientSideAssignationDestination = recordingConfiguration.getClientSideAssignationDestination();
            if (clientSideAssignationDestination != null) {
                createAssignation(filledElement, clientSideAssignationDestination, RECORD_FORM_ITEM_NAME);
            }

            createScript(filledElement, RIVR_SCOPE_OBJECT
                                        + ".addRecordingResult(dialog."
                                        + RECORD_FORM_ITEM_NAME
                                        + ", dialog."
                                        + RECORD_FORM_ITEM_NAME
                                        + "$, "
                                        + recordingConfiguration.isPostAudioToServer()
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

    private static void renderPrompts(InteractionPrompt interactionPrompt,
                                      Element parentElement,
                                      VoiceXmlDialogueContext voiceXmlDialogueContext,
                                      boolean bargeIn) throws VoiceXmlDocumentRenderingException {
        List<? extends AudioItem> audioItems = interactionPrompt.getAudioItems();
        createPrompt(interactionPrompt.getLanguage(), parentElement, voiceXmlDialogueContext, bargeIn, audioItems);
    }

    private SpeechRecognitionConfiguration factorizeGlobalSpeechConfiguration() {

        List<SpeechRecognitionConfiguration> speechConfigurations = getSpeechRecognitionConfigurations();
        if (speechConfigurations.isEmpty()) return null;

        SpeechRecognitionConfiguration speechGlobalConfiguration = speechConfigurations.get(0).copy();
        // unfortunately, we cannot use form-level grammars because of VoiceXML
        // semantic mapping (section 3.1.6)
        speechGlobalConfiguration.setGrammarItems(new GrammarItem[0]);

        for (SpeechRecognitionConfiguration speechConfiguration : speechConfigurations) {
            SpeechRecognitionConfiguration configuration = speechConfiguration;
            Assert.notNull(configuration, "configuration");

            if (!same(configuration.getCompleteTimeout(), speechGlobalConfiguration.getCompleteTimeout())) {
                speechGlobalConfiguration.setCompleteTimeout(null);
            }

            if (!same(configuration.getIncompleteTimeout(), speechGlobalConfiguration.getIncompleteTimeout())) {
                speechGlobalConfiguration.setIncompleteTimeout(null);
            }

            if (!same(configuration.getConfidenceLevel(), speechGlobalConfiguration.getConfidenceLevel())) {
                speechGlobalConfiguration.setConfidenceLevel(null);
            }

            if (!same(configuration.getMaxSpeechTimeout(), speechGlobalConfiguration.getMaxSpeechTimeout())) {
                speechGlobalConfiguration.setMaxSpeechTimeout(null);
            }

            if (!same(configuration.getSensitivity(), speechGlobalConfiguration.getSensitivity())) {
                speechGlobalConfiguration.setSensitivity(null);
            }

            if (!same(configuration.getSpeedVersusAccuracy(), speechGlobalConfiguration.getSpeedVersusAccuracy())) {
                speechGlobalConfiguration.setSpeedVersusAccuracy(null);
            }

            if (!same(configuration.getMaxNBest(), speechGlobalConfiguration.getMaxNBest())) {
                speechGlobalConfiguration.setMaxNBest(null);
            }

            factorizeParameters(speechGlobalConfiguration, configuration);
        }

        return speechGlobalConfiguration;
    }

    private DtmfRecognitionConfiguration factorizeGlobalDtmfConfiguration() {
        List<DtmfRecognitionConfiguration> dtmfConfigurations = getDtmfRecognitionConfigurations();
        if (dtmfConfigurations.isEmpty()) return null;

        DtmfRecognitionConfiguration dtmfGlobalConfiguration = dtmfConfigurations.get(0).copy();
        // unfortunately, we cannot use form-level grammars because of VoiceXML
        // semantic mapping (section 3.1.6)
        dtmfGlobalConfiguration.setGrammarItems(new GrammarItem[0]);

        for (DtmfRecognitionConfiguration dtmfConfiguration : dtmfConfigurations) {
            DtmfRecognitionConfiguration configuration = dtmfConfiguration;
            Assert.notNull(configuration, "configuration");

            if (!same(configuration.getInterDigitTimeout(), dtmfGlobalConfiguration.getInterDigitTimeout())) {
                dtmfGlobalConfiguration.setInterDigitTimeout(null);
            }

            if (!same(configuration.getTermTimeout(), dtmfGlobalConfiguration.getTermTimeout())) {
                dtmfGlobalConfiguration.setTermTimeout(null);
            }

            if (!same(configuration.getTermChar(), dtmfGlobalConfiguration.getTermChar())) {
                dtmfGlobalConfiguration.setTermChar(null);
            }

            factorizeParameters(dtmfGlobalConfiguration, configuration);
        }

        return dtmfGlobalConfiguration;
    }

    private List<SpeechRecognitionConfiguration> getSpeechRecognitionConfigurations() {

        List<SpeechRecognitionConfiguration> configurations = new ArrayList<SpeechRecognitionConfiguration>();

        for (InteractionPrompt prompt : mPrompts) {
            SpeechRecognitionConfiguration speechRecognitionConfiguration = prompt.getSpeechRecognitionConfiguration();
            if (speechRecognitionConfiguration != null) {
                configurations.add(speechRecognitionConfiguration);
            }

        }

        if (mRecognition != null) {
            SpeechRecognitionConfiguration speechRecognitionConfiguration = mRecognition.getSpeechRecognitionConfiguration();
            if (speechRecognitionConfiguration != null) {
                configurations.add(speechRecognitionConfiguration);
            }
        }

        return configurations;
    }

    private List<DtmfRecognitionConfiguration> getDtmfRecognitionConfigurations() {
        List<DtmfRecognitionConfiguration> configurations = new ArrayList<DtmfRecognitionConfiguration>();

        for (InteractionPrompt prompt : getPrompts()) {
            DtmfRecognitionConfiguration dtmfRecognitionConfiguration = prompt.getDtmfRecognitionConfiguration();
            if (dtmfRecognitionConfiguration != null) {
                configurations.add(dtmfRecognitionConfiguration);
            }

        }

        if (mRecognition != null) {
            DtmfRecognitionConfiguration dtmfRecognitionConfiguration = mRecognition.getDtmfRecognitionConfiguration();
            if (dtmfRecognitionConfiguration != null) {
                configurations.add(dtmfRecognitionConfiguration);
            }
        }

        return configurations;
    }

    private static boolean same(Object object1, Object object2) {
        if (object1 == null) return object2 == null;
        if (object2 == null) return false;
        return object1.equals(object2);
    }

    private static void factorizeParameters(RecognitionConfiguration globalConfiguration,
                                            RecognitionConfiguration configuration) {
        for (String propertyName : globalConfiguration.getPropertyNames()) {

            if (!same(configuration.getProperty(propertyName), globalConfiguration.getProperty(propertyName))) {
                globalConfiguration.removeProperty(propertyName);
            }
        }
    }

    private static void addBargeIn(InteractionPrompt prompt,
                                   DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                   SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                   Element formItemElement) {
        BargeInType bargeInType = prompt.getBargeInType();
        if (bargeInType != null) {
            addProperty(formItemElement, BARGE_IN_TYPE_PROPERTY, bargeInType.getKey());
        }

        if (dtmfRecognitionConfiguration != null && speechRecognitionConfiguration != null) {
            addProperty(formItemElement, INPUT_MODES_PROPERTY, DTMF_VOICE_INPUT_MODE);
        } else if (dtmfRecognitionConfiguration == null) {
            addProperty(formItemElement, INPUT_MODES_PROPERTY, VOICE_INPUT_MODE);
        } else if (speechRecognitionConfiguration == null) {
            addProperty(formItemElement, INPUT_MODES_PROPERTY, DTMF_INPUT_MODE);
        }
    }

    private static DtmfRecognitionConfiguration getLocalConfiguration(DtmfRecognitionConfiguration configuration,
                                                                      DtmfRecognitionConfiguration globalConfiguration) {
        if (configuration == null) return null;

        DtmfRecognitionConfiguration localConfiguration = configuration.copy();

        if (globalConfiguration.getInterDigitTimeout() != null) {
            localConfiguration.setInterDigitTimeout(null);
        }

        if (globalConfiguration.getTermTimeout() != null) {
            localConfiguration.setTermTimeout(null);
        }

        if (globalConfiguration.getTermChar() != null) {
            localConfiguration.setTermChar(null);
        }

        removeGlobalParameters(globalConfiguration, localConfiguration);

        return localConfiguration;
    }

    private static SpeechRecognitionConfiguration getLocalConfiguration(SpeechRecognitionConfiguration configuration,
                                                                        SpeechRecognitionConfiguration globalConfiguration) {
        if (configuration == null) return null;

        SpeechRecognitionConfiguration localConfiguration = configuration.copy();

        if (globalConfiguration.getCompleteTimeout() != null) {
            localConfiguration.setCompleteTimeout(null);
        }

        if (globalConfiguration.getIncompleteTimeout() != null) {
            localConfiguration.setIncompleteTimeout(null);
        }

        if (globalConfiguration.getConfidenceLevel() != null) {
            localConfiguration.setConfidenceLevel(null);
        }

        if (globalConfiguration.getIncompleteTimeout() != null) {
            localConfiguration.setIncompleteTimeout(null);
        }

        if (globalConfiguration.getMaxNBest() != null) {
            localConfiguration.setMaxNBest(null);
        }

        if (globalConfiguration.getMaxSpeechTimeout() != null) {
            localConfiguration.setMaxSpeechTimeout(null);
        }

        if (globalConfiguration.getSensitivity() != null) {
            localConfiguration.setSensitivity(null);
        }

        if (globalConfiguration.getSpeedVersusAccuracy() != null) {
            localConfiguration.setSpeedVersusAccuracy(null);
        }

        removeGlobalParameters(globalConfiguration, localConfiguration);

        return localConfiguration;
    }

    private static void removeGlobalParameters(RecognitionConfiguration globalConfiguration,
                                               RecognitionConfiguration localConfiguration) {
        for (String propertyName : globalConfiguration.getPropertyNames()) {
            localConfiguration.removeProperty(propertyName);
        }
    }

    private void createFormLevelFilled(Element parent) throws VoiceXmlDocumentRenderingException {
        Element filledElement = DomUtils.appendNewElement(parent, FILLED_ELEMENT);
        filledElement.setAttribute(MODE_ATTRIBUTE, ANY_MODE);

        if (mRecognition != null) {
            List<? extends AudioItem> acknowledgeAudioItems = mRecognition.getAcknowledgeAudioItems();
            if (!acknowledgeAudioItems.isEmpty()) {
                processAudioItems(acknowledgeAudioItems, filledElement);
            }
        }

        createScript(filledElement, RIVR_SCOPE_OBJECT + ".addRecognitionResult()");
        createGotoSubmit(filledElement);
    }
}