/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.fetch;

import javax.json.*;

import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class SubdialogueFetchConfiguration implements JsonSerializable {

    private static final String RESOURCE_FETCH_CONFIGURATION = "resourceFetchConfiguration";
    private static final String FETCH_AUDIO = "fetchAudio";

    private ResourceFetchConfiguration mResourceFetchConfiguration;
    private Recording mFetchAudio;

    public ResourceFetchConfiguration getResourceFetchConfiguration() {
        return mResourceFetchConfiguration;
    }

    public Recording getFetchAudio() {
        return mFetchAudio;
    }

    public void setResourceFetchConfiguration(ResourceFetchConfiguration resourceFetchConfiguration) {
        mResourceFetchConfiguration = resourceFetchConfiguration;
    }

    public void setFetchAudio(Recording fetchAudio) {
        mFetchAudio = fetchAudio;
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, FETCH_AUDIO, mFetchAudio);
        JsonUtils.add(builder, RESOURCE_FETCH_CONFIGURATION, mResourceFetchConfiguration);
        return builder.build();
    }
}