/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

import javax.json.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.output.interaction.*;
import com.nuecho.rivr.voicexml.turn.output.object.*;
import com.nuecho.rivr.voicexml.turn.output.subdialogue.*;
import com.nuecho.rivr.voicexml.turn.output.transfer.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * This abstract class is the superclass of all classes representing a turn
 * interpreted by the VoiceXML platform.
 * 
 * @author Nu Echo Inc.
 * @see InteractionTurn
 * @see MessageTurn
 * @see ObjectTurn
 * @see ScriptExecutionTurn
 * @see SubdialogueInvocationTurn
 * @see TransferTurn
 */
public abstract class VoiceXmlOutputTurn extends VoiceXmlDocumentTurn implements OutputTurn {
    private static final String OUTPUT_TURN_TYPE_PROPERTY = "outputTurnType";

    /**
     * @param name The name of this turn. Not empty.
     */
    public VoiceXmlOutputTurn(String name) {
        super(name);
    }

    protected abstract String getOuputTurnType();

    @Override
    protected final void putAdditionalTopLevelData(JsonObjectBuilder builder) {
        JsonUtils.add(builder, OUTPUT_TURN_TYPE_PROPERTY, getOuputTurnType());
    }
}