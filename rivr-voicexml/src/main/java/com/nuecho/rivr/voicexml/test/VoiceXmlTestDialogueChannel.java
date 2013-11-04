/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.test;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.channel.synchronous.step.*;
import com.nuecho.rivr.core.test.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.first.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.turn.output.interaction.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class VoiceXmlTestDialogueChannel
        extends
        TestDialogueChannel<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> {

    private static final String CONFIDENCE_PROPERTY = "confidence";
    private static final String INTERPRETATION_PROPERTY = "interpretation";
    private static final String INPUTMODE_PROPERTY = "inputmode";
    private static final String UTTERANCE_PROPERTY = "utterance";

    public VoiceXmlTestDialogueChannel(String name, TimeValue defaultTimeout) {
        super(name, defaultTimeout);
    }

    public final Interaction getLastInteraction() {
        OutputTurn lastOutputTurn = getLastStepAsOutputTurn();
        if (lastOutputTurn instanceof Interaction) return (Interaction) getLastStepAsOutputTurn();
        throw new AssertionError("Last output turn was not an interaction. Last step=" + lastOutputTurn);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processDtmfRecognition(String dtmfString) {
        return processDtmfRecognition(dtmfString, JsonValue.NULL, null);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processDtmfRecognition(String dtmfString,
                                                                                   JsonValue interpretation,
                                                                                   MarkInfo markInfo) {
        JsonArrayBuilder builder = JsonUtils.createArrayBuilder();
        JsonUtils.add(builder, createDtmfHypothesis(dtmfString, interpretation));
        return processRecognition(new RecognitionInfo(builder.build(), markInfo));
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processRecognition(JsonArray recognitionResult) {
        return processRecognition(new RecognitionInfo(recognitionResult, null));
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processRecognition(RecognitionInfo recognitionInfo) {
        VoiceXmlInputTurn inputTurn = new VoiceXmlInputTurn();
        inputTurn.setRecognitionInfo(recognitionInfo);
        return processInputTurn(inputTurn);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processScript(JsonValue value) {
        return processValue(value);
    }

    public Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processValue(JsonValue value) {
        VoiceXmlInputTurn inputTurn = new VoiceXmlInputTurn();
        inputTurn.setJsonValue(value);
        return processInputTurn(inputTurn);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processRecording(RecordingInfo recordingInfo) {
        VoiceXmlInputTurn inputTurn = new VoiceXmlInputTurn();
        inputTurn.setRecordingInfo(recordingInfo);
        return processInputTurn(inputTurn);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processNoAction() {
        return processInputTurn(new VoiceXmlInputTurn());
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processNoMatch() {
        return processEvent(VoiceXmlEvent.NO_MATCH);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processNoInput() {
        return processEvent(VoiceXmlEvent.NO_INPUT);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processMaxSpeechTimeout() {
        return processEvent(VoiceXmlEvent.MAX_SPEECH_TIMEOUT);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processHangup() {
        return processEvent(VoiceXmlEvent.CONNECTION_DISCONNECT_HANGUP);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processPlatformError() {
        return processEvent(VoiceXmlEvent.ERROR);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processTransferResult(TransferStatusInfo transferStatusInfo) {
        VoiceXmlInputTurn inputTurn = new VoiceXmlInputTurn();
        inputTurn.setTransferResult(transferStatusInfo);
        return processInputTurn(inputTurn);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processTransferInvalidDestinationResult() {
        return processEvent(VoiceXmlEvent.ERROR_CONNECTION_BAD_DESTINATION);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processTransferDisconnect() {
        return processEvent(VoiceXmlEvent.CONNECTION_DISCONNECT_TRANSFER);
    }

    public final Step<VoiceXmlOutputTurn, VoiceXmlLastTurn> processEvent(String event) {
        VoiceXmlInputTurn inputTurn = new VoiceXmlInputTurn();
        inputTurn.setEvents(createSingleEventList(event));
        return processInputTurn(inputTurn);
    }

    private ArrayList<VoiceXmlEvent> createSingleEventList(String eventName) {
        VoiceXmlEvent event = new VoiceXmlEvent(eventName, null);
        return new ArrayList<VoiceXmlEvent>(Collections.singleton(event));
    }

    private JsonObject createDtmfHypothesis(String dtmfString, JsonValue interpretation) {
        return createHypothesis(dtmfString, interpretation, InputMode.dtmf, 1.0);
    }

    private JsonObject createHypothesis(String uterance, JsonValue interpretation, InputMode inputMode, double confidence) {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, UTTERANCE_PROPERTY, uterance);
        JsonUtils.add(builder, INPUTMODE_PROPERTY, inputMode.name());
        JsonUtils.add(builder, INTERPRETATION_PROPERTY, interpretation);
        builder.add(CONFIDENCE_PROPERTY, confidence);
        return builder.build();
    }
}
