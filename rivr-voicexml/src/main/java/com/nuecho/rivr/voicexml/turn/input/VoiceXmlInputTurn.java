/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.input;

import static java.util.Collections.*;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.servlet.*;
import com.nuecho.rivr.voicexml.util.json.*;

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

}