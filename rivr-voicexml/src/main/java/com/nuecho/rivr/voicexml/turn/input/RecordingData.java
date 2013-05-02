package com.nuecho.rivr.voicexml.turn.input;

import javax.json.*;

import com.nuecho.rivr.voicexml.util.json.*;

public final class RecordingData implements JsonSerializable {

    private static final String SIZE_PROPERTY = "size";
    private static final String TYPE_PROPERTY = "type";
    private static final String FILENAME_PROPERTY = "filename";

    private final byte[] mAudioPayLoad;
    private final String mType;
    private final String mFilename;

    public RecordingData(byte[] audioPayLoad, String type, String fileName) {
        mAudioPayLoad = audioPayLoad;
        mType = type;
        mFilename = fileName;
    }

    public byte[] getAudioPayLoad() {
        return mAudioPayLoad;
    }

    public String getType() {
        return mType;
    }

    public String getFilename() {
        return mFilename;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, FILENAME_PROPERTY, mFilename);
        JsonUtils.add(builder, TYPE_PROPERTY, mType);
        if (mAudioPayLoad != null) {
            builder.add(SIZE_PROPERTY, (long) mAudioPayLoad.length);
        }
        //payload is not serialized
        return builder.build();
    }
}