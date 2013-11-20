/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output;

/**
 * @author Nu Echo Inc.
 */
public final class OutputTurns {

    private OutputTurns() {
        //utility class: instantiation forbidden
    }

    public static Interaction.Builder interaction(String name) {
        return new Interaction.Builder(name);
    }

    public static Message.Builder message(String name) {
        return new Message.Builder(name);
    }

    public static Script.Builder script(String name) {
        return new Script.Builder(name);
    }

    public static ObjectCall.Builder objectCall(String name) {
        return new ObjectCall.Builder(name);
    }

    public static SubdialogueCall.Builder subdialogueCall(String name) {
        return new SubdialogueCall.Builder(name);
    }

    public static BlindTransfer.Builder blindTransfer(String name) {
        return new BlindTransfer.Builder(name);
    }

    public static BridgeTransfer.Builder bridgeTransfer(String name) {
        return new BridgeTransfer.Builder(name);
    }

    public static ConsultationTransfer.Builder consultationTransfer(String name) {
        return new ConsultationTransfer.Builder(name);
    }

}
