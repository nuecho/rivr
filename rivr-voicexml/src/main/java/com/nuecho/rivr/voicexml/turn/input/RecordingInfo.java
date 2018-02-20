/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.input;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.servlet.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.turn.output.Interaction.FinalRecordingWindow;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * {@link RecordingInfo} contains the recorded file, its duration and other meta
 * information produced by an {@link Interaction} with a
 * {@link FinalRecordingWindow}.
 * 
 * @author Nu Echo Inc.
 * @see Interaction
 * @see FinalRecordingWindow
 * @see Recording
 * @see FileUpload
 */
public final class RecordingInfo implements JsonSerializable {

    private static final String DURATION_PROPERTY = "duration";
    private static final String MAX_TIME_PROPERTY = "maxTime";
    private static final String DTMF_TERM_PROPERTY = "dtmfTerm";

    private final FileUpload mFile;
    private final Duration mDuration;
    private final boolean mMaxTime;
    private final String mDtmfTerm;

    public RecordingInfo(FileUpload file, Duration duration, boolean maxTime, String dtmfTerm) {
        mFile = file;
        mDuration = duration;
        mMaxTime = maxTime;
        mDtmfTerm = dtmfTerm;
    }

    public FileUpload getFile() {
        return mFile;
    }

    public boolean isMaxTime() {
        return mMaxTime;
    }

    public String getDtmfTerm() {
        return mDtmfTerm;
    }

    public Duration getDuration() {
        return mDuration;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, DTMF_TERM_PROPERTY, mDtmfTerm);
        builder.add(MAX_TIME_PROPERTY, mMaxTime);
        builder.add(DURATION_PROPERTY, mDuration.getMilliseconds());
        JsonUtils.add(builder, "file", mFile);
        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mDtmfTerm == null) ? 0 : mDtmfTerm.hashCode());
        result = prime * result + ((mDuration == null) ? 0 : mDuration.hashCode());
        result = prime * result + ((mFile == null) ? 0 : mFile.hashCode());
        result = prime * result + (mMaxTime ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RecordingInfo other = (RecordingInfo) obj;
        if (mDtmfTerm == null) {
            if (other.mDtmfTerm != null) return false;
        } else if (!mDtmfTerm.equals(other.mDtmfTerm)) return false;
        if (mDuration == null) {
            if (other.mDuration != null) return false;
        } else if (!mDuration.equals(other.mDuration)) return false;
        if (mFile == null) {
            if (other.mFile != null) return false;
        } else if (!mFile.equals(other.mFile)) return false;
        if (mMaxTime != other.mMaxTime) return false;
        return true;
    }

}