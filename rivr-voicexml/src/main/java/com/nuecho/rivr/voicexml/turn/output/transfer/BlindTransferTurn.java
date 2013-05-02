/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.transfer;

/**
 * @author Nu Echo Inc.
 */
public class BlindTransferTurn extends TransferTurn {

    public static final String TYPE = "blind";

    public BlindTransferTurn(String name, String destination) {
        super(name, destination);
    }

    @Override
    public String getTransferType() {
        return TYPE;
    }

}