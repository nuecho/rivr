/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.transfer;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.turn.output.interaction.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class SupervisedTransferTurn extends TransferTurn {

    private static final String SPEECH_RECOGNITION_CONFIGURATION_PROPERTY = "speechRecognitionConfiguration";
    private static final String DTMF_RECOGNITION_CONFIGURATION_PROPERTY = "dtmfRecognitionConfiguration";
    private static final String TRANSFER_AUDIO_PROPERTY = "transferAudio";
    private static final String CONNECT_TIMEOUT_PROPERTY = "connectTimeout";

    private DtmfRecognitionConfiguration mDtmfRecognitionConfiguration;
    private SpeechRecognitionConfiguration mSpeechRecognitionConfiguration;

    private Recording mTransferAudio;
    private TimeValue mConnectTimeout;

    public SupervisedTransferTurn(String name, String destination) {
        super(name, destination);
    }

    public void setTransferAudio(Recording transferAudio) {
        mTransferAudio = transferAudio;
    }

    public Recording getTransferAudio() {
        return mTransferAudio;
    }

    public TimeValue getConnectTimeout() {
        return mConnectTimeout;
    }

    public void setConnectTimeout(TimeValue connectTimeout) {
        mConnectTimeout = connectTimeout;
    }

    public DtmfRecognitionConfiguration getDtmfRecognitionConfiguration() {
        return mDtmfRecognitionConfiguration;
    }

    public void setDtmfRecognitionConfiguration(DtmfRecognitionConfiguration dtmfRecognitionConfiguration) {
        mDtmfRecognitionConfiguration = dtmfRecognitionConfiguration;
    }

    public SpeechRecognitionConfiguration getSpeechRecognitionConfiguration() {
        return mSpeechRecognitionConfiguration;
    }

    public void setSpeechRecognitionConfiguration(SpeechRecognitionConfiguration speechRecognitionConfiguration) {
        mSpeechRecognitionConfiguration = speechRecognitionConfiguration;
    }

    @Override
    protected void customizeTransferElement(Element transferElement) throws VoiceXmlDocumentRenderingException {
        VoiceXmlDomUtil.processDtmfConfiguration(mDtmfRecognitionConfiguration, transferElement);
        VoiceXmlDomUtil.processSpeechConfiguration(mSpeechRecognitionConfiguration, transferElement);
        setTimeAttribute(transferElement, CONNECT_TIMEOUT_ATTRIBUTE, mConnectTimeout);

        if (mTransferAudio != null) {
            setAttribute(transferElement, TRANSFER_AUDIO_ATTRIBUTE, mTransferAudio.getPath());
        }
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.addTimeProperty(builder, CONNECT_TIMEOUT_PROPERTY, mConnectTimeout);
        JsonUtils.add(builder, TRANSFER_AUDIO_PROPERTY, mTransferAudio);
        JsonUtils.add(builder, DTMF_RECOGNITION_CONFIGURATION_PROPERTY, mDtmfRecognitionConfiguration);
        JsonUtils.add(builder, SPEECH_RECOGNITION_CONFIGURATION_PROPERTY, mSpeechRecognitionConfiguration);
    }
}