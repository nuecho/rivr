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

    /**
     * Checks if a parameter has been provided.
     *
     * @param key the parameter name
     * @return true if the parameter exists
     * @since 1.0.1
     */
    public boolean hasParameter(String key) {
        return mParameters.containsKey(key);
    }

    /**
     * Checks if a parameter has been provided.
     *
     * @param key the parameter name
     * @return true if the parameter exists
     * @deprecated use {@link #hasParameter(String)} instead
     */
    @Deprecated
    public boolean hasParameter(Object key) {
        return mParameters.containsKey(key);
    }

    /**
     * Get a parameter.
     *
     * @param key the parameter name
     * @return the parameter value
     * @deprecated use {@link #getParameter(String)} instead
     */
    @Deprecated
    public String getParameters(Object key) {
        return mParameters.get(key);
    }

    /**
     * Get a parameter.
     *
     * @param key the parameter name
     * @return the parameter value
     * @since 1.0.1
     */
    public String getParameter(String key) {
        return mParameters.get(key);
    }

    /**
     * Get the list of parameter names.
     *
     * @return the parameter names
     * @since 1.0.1
     */
    public Set<String> keySet() {
        return mParameters.keySet();
    }
}
