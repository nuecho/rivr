/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>Recording</code> represents the description of a
 * recording final phase of an interaction.
 * <p>
 * It may have an optional {@link DtmfRecognition} to recognize
 * DTMF input while recording. A DTMF input matching one of the
 * {@link DtmfRecognition} grammar will terminate the recording and
 * place the recording in a variable.
 * 
 * @author Nu Echo Inc.
 * @see DtmfRecognition
 * @see <a href="http://www.w3.org/TR/voicexml20/#dml2.3.6">http://www.w3.org/TR/voicexml20/#dml2.3.6</a>
 */
public final class Recording implements JsonSerializable {
    private static final String DTMF_TERM_PROPERTY = "dtmfTerm";
    private static final String BEEP_PROPERTY = "beep";
    private static final String POST_AUDIO_TO_SERVER_PROPERTY = "postAudioToServer";
    private static final String MAXIMUM_TIME_PROPERTY = "maximumTime";
    private static final String FINAL_SILENCE_PROPERTY = "finalSilence";
    private static final String DTMF_TERM_RECOGNITION_PROPERTY = "dtmfTermRecognition";
    private static final String CLIENT_SIDE_ASSIGNATION_DESTINATION_PROPERTY = "clientSideAssignationDestination";
    private static final String TYPE_PROPERTY = "type";

    private DtmfRecognition mDtmfTermRecognition;
    private Boolean mBeep;
    private TimeValue mMaximumTime;
    private TimeValue mFinalSilence;
    private String mType;
    private String mClientSideAssignationDestination;
    private Boolean mDtmfTerm;
    private boolean mPostAudioToServer = true;

    /**
     * @param dtmfRecognition The
     *            {@link DtmfRecognition} used to interrupt the
     *            recording. Null reverts to VoiceXML default value.
     */
    public void setDtmfTermRecognition(DtmfRecognition dtmfRecognition) {
        mDtmfTermRecognition = dtmfRecognition;
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
     * @see <a href="http://www.w3.org/TR/voicexml20/#dmlAAudioFormats">http://www.w3.org/TR/voicexml20/#dmlAAudioFormats</a>
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
     *            made available in the <code>file</code> property of the
     *            {@link RecordingInfo}.
     */
    public void setPostAudioToServer(boolean postAudioToServer) {
        mPostAudioToServer = postAudioToServer;
    }

    public DtmfRecognition getDtmfTermRecognition() {
        return mDtmfTermRecognition;
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
        JsonUtils.add(builder, DTMF_TERM_RECOGNITION_PROPERTY, mDtmfTermRecognition);
        JsonUtils.add(builder, TYPE_PROPERTY, mType);
        JsonUtils.add(builder, CLIENT_SIDE_ASSIGNATION_DESTINATION_PROPERTY, mClientSideAssignationDestination);
        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mBeep == null) ? 0 : mBeep.hashCode());
        result = prime
                 * result
                 + ((mClientSideAssignationDestination == null) ? 0 : mClientSideAssignationDestination.hashCode());
        result = prime * result + ((mDtmfTerm == null) ? 0 : mDtmfTerm.hashCode());
        result = prime
                 * result
                 + ((mDtmfTermRecognition == null) ? 0 : mDtmfTermRecognition.hashCode());
        result = prime * result + ((mFinalSilence == null) ? 0 : mFinalSilence.hashCode());
        result = prime * result + ((mMaximumTime == null) ? 0 : mMaximumTime.hashCode());
        result = prime * result + (mPostAudioToServer ? 1231 : 1237);
        result = prime * result + ((mType == null) ? 0 : mType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Recording other = (Recording) obj;
        if (mBeep == null) {
            if (other.mBeep != null) return false;
        } else if (!mBeep.equals(other.mBeep)) return false;
        if (mClientSideAssignationDestination == null) {
            if (other.mClientSideAssignationDestination != null) return false;
        } else if (!mClientSideAssignationDestination.equals(other.mClientSideAssignationDestination)) return false;
        if (mDtmfTerm == null) {
            if (other.mDtmfTerm != null) return false;
        } else if (!mDtmfTerm.equals(other.mDtmfTerm)) return false;
        if (mDtmfTermRecognition == null) {
            if (other.mDtmfTermRecognition != null) return false;
        } else if (!mDtmfTermRecognition.equals(other.mDtmfTermRecognition)) return false;
        if (mFinalSilence == null) {
            if (other.mFinalSilence != null) return false;
        } else if (!mFinalSilence.equals(other.mFinalSilence)) return false;
        if (mMaximumTime == null) {
            if (other.mMaximumTime != null) return false;
        } else if (!mMaximumTime.equals(other.mMaximumTime)) return false;
        if (mPostAudioToServer != other.mPostAudioToServer) return false;
        if (mType == null) {
            if (other.mType != null) return false;
        } else if (!mType.equals(other.mType)) return false;
        return true;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }
}