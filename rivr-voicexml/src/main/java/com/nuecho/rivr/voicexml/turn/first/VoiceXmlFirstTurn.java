/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.first;

import java.util.*;

import com.nuecho.rivr.core.channel.*;

/**
 * @author Nu Echo Inc.
 */
public class VoiceXmlFirstTurn implements FirstTurn {

    private final Map<String, String> mParameters;

    public VoiceXmlFirstTurn() {
        mParameters = Collections.emptyMap();
    }

    public VoiceXmlFirstTurn(Map<String, String> parameters) {
        mParameters = new HashMap<String, String>(parameters);
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(mParameters);
    }

    public boolean hasParameter(Object key) {
        return mParameters.containsKey(key);
    }

    public String getParameters(Object key) {
        return mParameters.get(key);
    }

    public Set<String> keySet() {
        return mParameters.keySet();
    }
}
