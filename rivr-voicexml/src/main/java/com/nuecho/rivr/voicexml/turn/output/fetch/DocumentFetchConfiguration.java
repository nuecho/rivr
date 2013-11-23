/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.fetch;

import javax.json.*;

import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class DocumentFetchConfiguration extends FetchConfiguration {

    private static final String FETCH_AUDIO_PROPERTY = "fetchAudio";

    private String mFetchAudio;

    public String getFetchAudio() {
        return mFetchAudio;
    }

    public void setFetchAudio(String fetchAudio) {
        mFetchAudio = fetchAudio;
    }

    public void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, FETCH_AUDIO_PROPERTY, mFetchAudio);
    }
}
