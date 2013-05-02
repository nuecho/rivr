/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

import javax.json.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Base class of all system-initiated turns.
 * 
 * @author Nu Echo Inc.
 */
public abstract class VoiceXmlOutputTurn extends VoiceXmlDocumentTurn implements OutputTurn {

    private static final String OUTPUT_TURN_TYPE_PROPERTY = "outputTurnType";

    public VoiceXmlOutputTurn(String name) {
        super(name);
    }

    protected abstract String getOuputTurnType();

    @Override
    protected void putAdditionalTopLevelData(JsonObjectBuilder builder) {
        JsonUtils.add(builder, OUTPUT_TURN_TYPE_PROPERTY, getOuputTurnType());
    }
}