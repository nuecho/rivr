/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.transfer;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.turn.output.interaction.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>SupervisedTransferTurn</code> is a {@link TransferTurn} where the
 * outcome is monitored and where a transfer failure can be handled gracefully.
 * <p>
 * Recognition can optionally be activated to cancel a supervised transfer.
 * 
 * @author Nu Echo Inc.
 * @see BridgeTransferTurn
 * @see ConsultationTransferTurn
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

    /**
     * @param name The name of this turn. Not empty.
     * @param destination The URI of the destination (telephone, IP telephony
     *            address). Not empty.
     */
    public SupervisedTransferTurn(String name, String destination) {
        super(name, destination);
    }

    /**
     * @param dtmfRecognitionConfiguration The active DTMF recognition
     *            configuration during the transfer.
     */
    public final void setDtmfRecognitionConfiguration(DtmfRecognitionConfiguration dtmfRecognitionConfiguration) {
        mDtmfRecognitionConfiguration = dtmfRecognitionConfiguration;
    }

    /**
     * @param speechRecognitionConfiguration The active speech recognition
     *            configuration during the transfer.
     */
    public final void setSpeechRecognitionConfiguration(SpeechRecognitionConfiguration speechRecognitionConfiguration) {
        mSpeechRecognitionConfiguration = speechRecognitionConfiguration;
    }

    /**
     * @param transferAudio The URI of audio source to play while the transfer
     *            attempt is in progress.
     */
    public final void setTransferAudio(Recording transferAudio) {
        mTransferAudio = transferAudio;
    }

    /**
     * @param connectTimeout The time to wait while trying to connect the call
     *            before returning with {@link TransferStatus#NO_ANSWER}. Null
     *            reverts to VoiceXML default value.
     */
    public final void setConnectTimeout(TimeValue connectTimeout) {
        mConnectTimeout = connectTimeout;
    }

    public final Recording getTransferAudio() {
        return mTransferAudio;
    }

    public final TimeValue getConnectTimeout() {
        return mConnectTimeout;
    }

    public final DtmfRecognitionConfiguration getDtmfRecognitionConfiguration() {
        return mDtmfRecognitionConfiguration;
    }

    public final SpeechRecognitionConfiguration getSpeechRecognitionConfiguration() {
        return mSpeechRecognitionConfiguration;
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
    protected void addTurnProperties(JsonObjectBuilder builder) {
        super.addTurnProperties(builder);
        JsonUtils.addTimeProperty(builder, CONNECT_TIMEOUT_PROPERTY, mConnectTimeout);
        JsonUtils.add(builder, TRANSFER_AUDIO_PROPERTY, mTransferAudio);
        JsonUtils.add(builder, DTMF_RECOGNITION_CONFIGURATION_PROPERTY, mDtmfRecognitionConfiguration);
        JsonUtils.add(builder, SPEECH_RECOGNITION_CONFIGURATION_PROPERTY, mSpeechRecognitionConfiguration);
    }
}