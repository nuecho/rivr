/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.input;

import static java.util.Collections.*;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.servlet.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * The result of an interaction with the platform, it may contains one or many
 * of the following: a recognition result, a recording, VoiceXml events, a
 * transfer status, a script result, uploaded files.
 * 
 * @author Nu Echo Inc.
 * @see RecognitionInfo
 * @see RecordingInfo
 * @see TransferStatusInfo
 * @see VoiceXmlEvent
 * @see FileUpload
 */
public final class VoiceXmlInputTurn implements InputTurn, JsonSerializable {

    private static final String TRANSFER_RESULT_PROPERTY = "transferResult";
    private static final String RECORDING_INFO_PROPERTY = "recordingInfo";
    private static final String RECOGNITION_INFO_PROPERTY = "recognitionInfo";
    private static final String EVENTS_PROPERTY = "events";
    private static final String VALUE_PROPERTY = "value";

    private List<VoiceXmlEvent> mEvents = emptyList();
    private JsonValue mJsonValue;
    private RecognitionInfo mRecognitionInfo;
    private RecordingInfo mRecordingInfo;
    private TransferStatusInfo mTransferResult;
    private Map<String, FileUpload> mFiles;

    public List<VoiceXmlEvent> getEvents() {
        return Collections.unmodifiableList(mEvents);
    }

    public JsonValue getJsonValue() {
        return mJsonValue;
    }

    public RecognitionInfo getRecognitionInfo() {
        return mRecognitionInfo;
    }

    public RecordingInfo getRecordingInfo() {
        return mRecordingInfo;
    }

    public TransferStatusInfo getTransferResult() {
        return mTransferResult;
    }

    public void setEvents(List<VoiceXmlEvent> events) {
        Assert.notNull(events, "events");
        mEvents = new ArrayList<VoiceXmlEvent>(events);
    }

    public void setJsonValue(JsonValue jsonValue) {
        mJsonValue = jsonValue;
    }

    public void setRecognitionInfo(RecognitionInfo recognitionInfo) {
        mRecognitionInfo = recognitionInfo;
    }

    public void setRecordingInfo(RecordingInfo recordingInfo) {
        mRecordingInfo = recordingInfo;
    }

    public void setTransferResult(TransferStatusInfo transferResult) {
        mTransferResult = transferResult;
    }

    public void setFiles(Map<String, FileUpload> files) {
        mFiles = new HashMap<String, FileUpload>(files);
    }

    public Map<String, FileUpload> getFiles() {
        return unmodifiableMap(mFiles);
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, VALUE_PROPERTY, mJsonValue);
        JsonUtils.add(builder, EVENTS_PROPERTY, JsonUtils.toJson(mEvents));
        JsonUtils.add(builder, RECOGNITION_INFO_PROPERTY, mRecognitionInfo);
        JsonUtils.add(builder, RECORDING_INFO_PROPERTY, mRecordingInfo);
        JsonUtils.add(builder, TRANSFER_RESULT_PROPERTY, mTransferResult);
        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mEvents == null) ? 0 : mEvents.hashCode());
        result = prime * result + ((mFiles == null) ? 0 : mFiles.hashCode());
        result = prime * result + ((mJsonValue == null) ? 0 : mJsonValue.hashCode());
        result = prime * result + ((mRecognitionInfo == null) ? 0 : mRecognitionInfo.hashCode());
        result = prime * result + ((mRecordingInfo == null) ? 0 : mRecordingInfo.hashCode());
        result = prime * result + ((mTransferResult == null) ? 0 : mTransferResult.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VoiceXmlInputTurn other = (VoiceXmlInputTurn) obj;
        if (mEvents == null) {
            if (other.mEvents != null) return false;
        } else if (!mEvents.equals(other.mEvents)) return false;
        if (mFiles == null) {
            if (other.mFiles != null) return false;
        } else if (!mFiles.equals(other.mFiles)) return false;
        if (mJsonValue == null) {
            if (other.mJsonValue != null) return false;
        } else if (!mJsonValue.equals(other.mJsonValue)) return false;
        if (mRecognitionInfo == null) {
            if (other.mRecognitionInfo != null) return false;
        } else if (!mRecognitionInfo.equals(other.mRecognitionInfo)) return false;
        if (mRecordingInfo == null) {
            if (other.mRecordingInfo != null) return false;
        } else if (!mRecordingInfo.equals(other.mRecordingInfo)) return false;
        if (mTransferResult == null) {
            if (other.mTransferResult != null) return false;
        } else if (!mTransferResult.equals(other.mTransferResult)) return false;
        return true;
    }

}