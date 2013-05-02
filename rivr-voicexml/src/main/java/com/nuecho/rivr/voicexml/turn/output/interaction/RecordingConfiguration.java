/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class RecordingConfiguration implements JsonSerializable {

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
    private boolean mPostAudioToServer = true;
    private Boolean mDtmfTerm;

    public TimeValue getMaximumTime() {
        return mMaximumTime;
    }

    public void setMaximumTime(TimeValue maximumTime) {
        mMaximumTime = maximumTime;
    }

    public TimeValue getFinalSilence() {
        return mFinalSilence;
    }

    public void setFinalSilence(TimeValue finalSilence) {
        mFinalSilence = finalSilence;
    }

    public Boolean getBeep() {
        return mBeep;
    }

    public void setBeep(boolean beep) {
        mBeep = Boolean.valueOf(beep);
    }

    public void resetBeep() {
        mBeep = null;
    }

    public Boolean getDtmfTerm() {
        return mDtmfTerm;
    }

    public void setDtmfTerm(boolean dtmfTerm) {
        mDtmfTerm = Boolean.valueOf(dtmfTerm);
    }

    public void resetDtmfTerm() {
        mDtmfTerm = null;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getClientSideAssignationDestination() {
        return mClientSideAssignationDestination;
    }

    public void setClientSideAssignationDestination(String clientSideAssignationDestination) {
        mClientSideAssignationDestination = clientSideAssignationDestination;
    }

    public boolean isPostAudioToServer() {
        return mPostAudioToServer;
    }

    public void setPostAudioToServer(boolean postAudioToServer) {
        mPostAudioToServer = postAudioToServer;
    }

    public DtmfRecognitionConfiguration getDtmfTermRecognitionConfiguration() {
        return mDtmfTermRecognitionConfiguration;
    }

    public void setDtmfTermRecognitionConfiguration(DtmfRecognitionConfiguration dtmfRecognitionConfiguration) {
        mDtmfTermRecognitionConfiguration = dtmfRecognitionConfiguration;
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