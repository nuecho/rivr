/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.transfer;

/**
 * A <code>BlindTransferTurn</code> is a {@link TransferTurn} where the outcome
 * is not monitored.
 * <p>
 * Once the transfer begins and the interpreter disconnects from the session,
 * the platform throws connection.disconnect.transfer.
 * 
 * @author Nu Echo Inc.
 * @see http://www.w3.org/TR/voicexml20/#dml2.3.7.1
 */
public class BlindTransferTurn extends TransferTurn {
    private static final String BLIND_TRANSFER_TYPE = "blind";

    /**
     * @param name The name of this turn. Not empty.
     * @param destination The URI of the destination (telephone, IP telephony
     *            address). Not empty.
     */
    public BlindTransferTurn(String name, String destination) {
        super(name, destination);
    }

    @Override
    protected final String getTransferType() {
        return BLIND_TRANSFER_TYPE;
    }
}