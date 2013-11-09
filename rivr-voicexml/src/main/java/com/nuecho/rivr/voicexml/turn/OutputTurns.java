/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn;

import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * @author Nu Echo Inc.
 */
public class OutputTurns {

    public Interaction.Builder interaction(String name) {
        return new Interaction.Builder(name);
    }

    public Message.Builder message(String name) {
        return new Message.Builder(name);
    }

    public Script.Builder script(String name) {
        return new Script.Builder(name);
    }

    public ObjectCall.Builder objectCall(String name) {
        return new ObjectCall.Builder(name);
    }

    public SubdialogueCall.Builder subdialogueCall(String name) {
        return new SubdialogueCall.Builder(name);
    }

    public BlindTransfer.Builder blindTransfer(String name) {
        return new BlindTransfer.Builder(name);
    }

    public BridgeTransfer.Builder bridgeTransfer(String name) {
        return new BridgeTransfer.Builder(name);
    }

    public ConsultationTransfer.Builder consultationTransfer(String name) {
        return new ConsultationTransfer.Builder(name);
    }

}
