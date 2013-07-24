/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>RecordingConfiguration</code> represents the description of a
 * recording final phase of an interaction.
 * <p>
 * It may have an optional {@link DtmfRecognitionConfiguration} to recognize
 * DTMF input while recording. A DTMF input matching one of the
 * {@link DtmfRecognitionConfiguration} grammar will terminate the recording and
 * place the recording in a variable.
 * 
 * @author Nu Echo Inc.
 * @see DtmfRecognitionConfiguration
 * @see http://www.w3.org/TR/voicexml20/#dml2.3.6
 */
public final class RecordingConfiguration implements JsonSerializable {
    private static final String DTMF_TERM_PROPERTY = "dtmfTerm";
    private static final String BEEP_PROPERTY = "beep";
    private static final String POST_AUDIO_TO_SERVER_PROPERTY = "postAudioToServer";
    private static final String MAXIMUM_TIME_PROPERTY = "maximumTime";
    private static final String FINAL_SILENCE_PROPERTY = "finalSilence";
    private static final String DTMF_TERM_RECOGNITION_CONFIGURATION_PROPERTY = "dtmfTermRecognitionConfiguration";
    private static final String CLIENT_SIDE_ASSIGNATION_DESTINATION_PROPERTY = "clientSideAssignationDestination";
    private static final String TYPE_PROPERTY = "type";

    private DtmfRecognitionConfiguration mDtmfTermRecognitionConfiguration;
    private Boolean mBeep;
    private TimeValue mMaximumTime;
    private TimeValue mFinalSilence;
    private String mType;
    private String mClientSideAssignationDestination;
    private Boolean mDtmfTerm;
    private boolean mPostAudioToServer = true;

    /**
     * @param dtmfRecognitionConfiguration The
     *            {@link DtmfRecognitionConfiguration} used to interrupt the
     *            recording. Null reverts to VoiceXML default value.
     */
    public void setDtmfTermRecognitionConfiguration(DtmfRecognitionConfiguration dtmfRecognitionConfiguration) {
        mDtmfTermRecognitionConfiguration = dtmfRecognitionConfiguration;
    }

    /**
     * @param beep If true, a tone is emitted just prior to recording. Null
     *            reverts to VoiceXML default value.
     */
    public void setBeep(Boolean beep) {
        mBeep = Boolean.valueOf(beep);
    }

    /**
     * @param maximumTime The maximum duration to record. Null reverts to
     *            VoiceXML default value.
     */
    public void setMaximumTime(TimeValue maximumTime) {
        mMaximumTime = maximumTime;
    }

    /**
     * @param finalSilence The interval of silence that indicates end of speech.
     *            Null reverts to VoiceXML default value.
     */
    public void setFinalSilence(TimeValue finalSilence) {
        mFinalSilence = finalSilence;
    }

    /**
     * @param type The media format of the resulting recording. Null reverts to
     *            VoiceXML default value.
     * @see http://www.w3.org/TR/voicexml20/#dmlAAudioFormats
     */
    public void setType(String type) {
        mType = type;
    }

    /**
     * @param clientSideAssignationDestination The variable where the recording
     *            will be stored.
     */
    public void setClientSideAssignationDestination(String clientSideAssignationDestination) {
        mClientSideAssignationDestination = clientSideAssignationDestination;
    }

    /**
     * @param dtmfTerm If true, any DTMF keypress not matched by an active
     *            grammar will be treated as a match of an active local DTMF
     *            grammar. Null reverts to VoiceXML default value.
     */
    public void setDtmfTerm(Boolean dtmfTerm) {
        mDtmfTerm = Boolean.valueOf(dtmfTerm);
    }

    /**
     * @param postAudioToServer If true, recording will be posted to server and
     *            made available in the {@link RecordingData} property of the
     *            {@link RecordingInfo}.
     */
    public void setPostAudioToServer(boolean postAudioToServer) {
        mPostAudioToServer = postAudioToServer;
    }

    public DtmfRecognitionConfiguration getDtmfTermRecognitionConfiguration() {
        return mDtmfTermRecognitionConfiguration;
    }

    public Boolean getBeep() {
        return mBeep;
    }

    public TimeValue getMaximumTime() {
        return mMaximumTime;
    }

    public TimeValue getFinalSilence() {
        return mFinalSilence;
    }

    public Boolean getDtmfTerm() {
        return mDtmfTerm;
    }

    public String getType() {
        return mType;
    }

    public String getClientSideAssignationDestination() {
        return mClientSideAssignationDestination;
    }

    public boolean isPostAudioToServer() {
        return mPostAudioToServer;
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.addBooleanProperty(builder, BEEP_PROPERTY, mBeep);
        JsonUtils.addBooleanProperty(builder, DTMF_TERM_PROPERTY, mDtmfTerm);
        JsonUtils.addBooleanProperty(builder, POST_AUDIO_TO_SERVER_PROPERTY, mPostAudioToServer);
        JsonUtils.addTimeProperty(builder, MAXIMUM_TIME_PROPERTY, mMaximumTime);
        JsonUtils.addTimeProperty(builder, FINAL_SILENCE_PROPERTY, mFinalSilence);
        JsonUtils.add(builder, DTMF_TERM_RECOGNITION_CONFIGURATION_PROPERTY, mDtmfTermRecognitionConfiguration);
        JsonUtils.add(builder, TYPE_PROPERTY, mType);
        JsonUtils.add(builder, CLIENT_SIDE_ASSIGNATION_DESTINATION_PROPERTY, mClientSideAssignationDestination);
        return builder.build();
    }

    @Override
    public String toString() {
        return asJson().toString();
    }
}