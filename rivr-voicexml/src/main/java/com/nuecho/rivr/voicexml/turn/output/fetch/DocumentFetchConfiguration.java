/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.fetch;

import javax.json.*;

import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class DocumentFetchConfiguration extends ResourceFetchConfiguration {

    private static final String FETCH_AUDIO_PROPERTY = "fetchAudio";

    private Recording mFetchAudio;

    public Recording getFetchAudio() {
        return mFetchAudio;
    }

    public void setFetchAudio(Recording fetchAudio) {
        mFetchAudio = fetchAudio;
    }

    public void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, FETCH_AUDIO_PROPERTY, mFetchAudio);
    }
}
