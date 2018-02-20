/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A {@link SupervisedTransfer} is a {@link Transfer} where the outcome is
 * monitored and where a transfer failure can be handled gracefully.
 * <p>
 * Recognition can optionally be activated to cancel a supervised transfer.
 * 
 * @author Nu Echo Inc.
 * @see BridgeTransfer
 * @see ConsultationTransfer
 */
public abstract class SupervisedTransfer extends Transfer {
    private static final String SPEECH_RECOGNITION_PROPERTY = "speechRecognition";
    private static final String DTMF_RECOGNITION_PROPERTY = "dtmfRecognition";
    private static final String TRANSFER_AUDIO_PROPERTY = "transferAudio";
    private static final String CONNECT_TIMEOUT_PROPERTY = "connectTimeout";

    private DtmfRecognition mDtmfRecognition;
    private SpeechRecognition mSpeechRecognition;

    private String mTransferAudio;
    private Duration mConnectTimeout;

    /**
     * @param name The name of this turn. Not empty.
     * @param destination The URI of the destination (telephone, IP telephony
     *            address). Not empty.
     */
    public SupervisedTransfer(String name, String destination) {
        super(name, destination);
    }

    /**
     * @param dtmfRecognition The active DTMF recognition configuration during
     *            the transfer.
     */
    public final void setDtmfRecognition(DtmfRecognition dtmfRecognition) {
        mDtmfRecognition = dtmfRecognition;
    }

    /**
     * @param speechRecognition The active speech recognition configuration
     *            during the transfer.
     */
    public final void setSpeechRecognition(SpeechRecognition speechRecognition) {
        mSpeechRecognition = speechRecognition;
    }

    /**
     * @param transferAudio The location (URI or path) of audio source to play
     *            while the transfer attempt is in progress.
     */
    public final void setTransferAudio(String transferAudio) {
        mTransferAudio = transferAudio;
    }

    /**
     * @param connectTimeout The time to wait while trying to connect the call
     *            before returning with {@link TransferStatus#NO_ANSWER}.
     *            <code>null</code> to use the VoiceXML platform default.
     */
    public final void setConnectTimeout(Duration connectTimeout) {
        mConnectTimeout = connectTimeout;
    }

    public final String getTransferAudio() {
        return mTransferAudio;
    }

    public final Duration getConnectTimeout() {
        return mConnectTimeout;
    }

    public final DtmfRecognition getDtmfRecognition() {
        return mDtmfRecognition;
    }

    public final SpeechRecognition getSpeechRecognition() {
        return mSpeechRecognition;
    }

    @Override
    protected void customizeTransferElement(Element transferElement) throws VoiceXmlDocumentRenderingException {
        VoiceXmlDomUtil.processDtmfRecognition(mDtmfRecognition, transferElement);
        VoiceXmlDomUtil.processSpeechRecognition(mSpeechRecognition, transferElement);
        setDurationAttribute(transferElement, CONNECT_TIMEOUT_ATTRIBUTE, mConnectTimeout);
        setAttribute(transferElement, TRANSFER_AUDIO_ATTRIBUTE, mTransferAudio);
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        super.addTurnProperties(builder);
        JsonUtils.addDurationProperty(builder, CONNECT_TIMEOUT_PROPERTY, mConnectTimeout);
        JsonUtils.add(builder, TRANSFER_AUDIO_PROPERTY, mTransferAudio);
        JsonUtils.add(builder, DTMF_RECOGNITION_PROPERTY, mDtmfRecognition);
        JsonUtils.add(builder, SPEECH_RECOGNITION_PROPERTY, mSpeechRecognition);
    }

    /**
     * Builder used to ease the creation of instances of
     * {@link SupervisedTransfer}
     */
    public abstract static class Builder extends Transfer.Builder {

        private DtmfRecognition mDtmfRecognition;
        private SpeechRecognition mSpeechRecognition;
        private String mTransferAudio;
        private Duration mConnectTimeout;

        protected Builder(String name) {
            super(name);
        }

        public Builder setDtmfRecognition(DtmfRecognition dtmfRecognition) {
            mDtmfRecognition = dtmfRecognition;
            return this;
        }

        public Builder setSpeechRecognition(SpeechRecognition speechRecognition) {
            mSpeechRecognition = speechRecognition;
            return this;
        }

        public Builder setTransferAudio(String transferAudio) {
            mTransferAudio = transferAudio;
            return this;
        }

        public Builder setConnectTimeout(Duration connectTimeout) {
            mConnectTimeout = connectTimeout;
            return this;
        }

        public void build(SupervisedTransfer supervisedTransfer) {
            supervisedTransfer.setConnectTimeout(mConnectTimeout);
            supervisedTransfer.setDtmfRecognition(mDtmfRecognition);
            supervisedTransfer.setSpeechRecognition(mSpeechRecognition);
            supervisedTransfer.setTransferAudio(mTransferAudio);
            super.build(supervisedTransfer);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((mConnectTimeout == null) ? 0 : mConnectTimeout.hashCode());
        result = prime * result + ((mDtmfRecognition == null) ? 0 : mDtmfRecognition.hashCode());
        result = prime * result + ((mSpeechRecognition == null) ? 0 : mSpeechRecognition.hashCode());
        result = prime * result + ((mTransferAudio == null) ? 0 : mTransferAudio.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        SupervisedTransfer other = (SupervisedTransfer) obj;
        if (mConnectTimeout == null) {
            if (other.mConnectTimeout != null) return false;
        } else if (!mConnectTimeout.equals(other.mConnectTimeout)) return false;
        if (mDtmfRecognition == null) {
            if (other.mDtmfRecognition != null) return false;
        } else if (!mDtmfRecognition.equals(other.mDtmfRecognition)) return false;
        if (mSpeechRecognition == null) {
            if (other.mSpeechRecognition != null) return false;
        } else if (!mSpeechRecognition.equals(other.mSpeechRecognition)) return false;
        if (mTransferAudio == null) {
            if (other.mTransferAudio != null) return false;
        } else if (!mTransferAudio.equals(other.mTransferAudio)) return false;
        return true;
    }

}