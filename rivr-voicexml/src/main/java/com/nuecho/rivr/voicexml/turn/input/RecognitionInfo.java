/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.input;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class RecognitionInfo implements JsonSerializable {

    private static final String MARK_INFO_PROPERTY = "markInfo";
    private static final String RECOGNITION_RESULT_PROPERTY = "recognitionResult";
    private final JsonArray mRecognitionResult;
    private final MarkInfo mMarkInfo;

    public RecognitionInfo(JsonArray recognitionResult, MarkInfo markInfo) {
        Assert.notNull(recognitionResult, RECOGNITION_RESULT_PROPERTY);
        mRecognitionResult = recognitionResult;
        mMarkInfo = markInfo;
    }

    public JsonArray getRecognitionResult() {
        return mRecognitionResult;
    }

    public MarkInfo getMarkInfo() {
        return mMarkInfo;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, RECOGNITION_RESULT_PROPERTY, mRecognitionResult);
        JsonUtils.add(builder, MARK_INFO_PROPERTY, mMarkInfo);
        return builder.build();
    }
}