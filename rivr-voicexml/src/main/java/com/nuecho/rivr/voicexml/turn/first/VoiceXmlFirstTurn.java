/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.first;

import java.util.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.voicexml.dialogue.*;

/**
 * First turn passed when running a {@link VoiceXmlDialogue}, it can be used to
 * consult the parameters from the initial request.
 * 
 * @author Nu Echo Inc.
 * @see VoiceXmlDialogue
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

    public boolean hasParameter(String key) {
        return mParameters.containsKey(key);
    }

    /**
     * @deprecated use {@link #hasParameter(String)} instead
     */
    @Deprecated
    public boolean hasParameter(Object key) {
        return mParameters.containsKey(key);
    }

    /**
     * @deprecated use {@link #getParameter(String)} instead
     */
    @Deprecated
    public String getParameters(Object key) {
        return mParameters.get(key);
    }

    public String getParameter(String key) {
        return mParameters.get(key);
    }

    public Set<String> keySet() {
        return mParameters.keySet();
    }
}
