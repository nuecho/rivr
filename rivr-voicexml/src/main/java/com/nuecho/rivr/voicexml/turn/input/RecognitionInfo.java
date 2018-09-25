/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.input;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * {@link RecognitionInfo} contains a JSON representation of the VoiceXml
 * recognition result (application.lastresult$) and optionally a
 * {@link MarkInfo} containing information about the last {@link Mark}
 * encountered before the recognition occurred.
 * 
 * @author Nu Echo Inc.
 * @see MarkInfo
 * @see Mark
 * @see <a
 *      href="https://www.w3.org/TR/voicexml20/#dml5.1.5">https://www.w3.org/TR/voicexml20/#dml5.1.5</a>
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mMarkInfo == null) ? 0 : mMarkInfo.hashCode());
        result = prime * result + ((mRecognitionResult == null) ? 0 : mRecognitionResult.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RecognitionInfo other = (RecognitionInfo) obj;
        if (mMarkInfo == null) {
            if (other.mMarkInfo != null) return false;
        } else if (!mMarkInfo.equals(other.mMarkInfo)) return false;
        if (mRecognitionResult == null) {
            if (other.mRecognitionResult != null) return false;
        } else if (!mRecognitionResult.equals(other.mRecognitionResult)) return false;
        return true;
    }
}