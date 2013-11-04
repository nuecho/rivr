/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

/**
 * A <code>ConsultationTransfer</code> is a is a {@link SupervisedTransfer} that
 * is similar to a {@link BlindTransfer} except that the outcome of the transfer
 * call setup is known and the caller is
 * not dropped as a result of an unsuccessful transfer attempt.
 * 
 * @author Nu Echo Inc.
 * @see <a href="http://www.w3.org/TR/voicexml21/#sec-xfer-consultation">http://www.w3.org/TR/voicexml21/#sec-xfer-consultation</a>
 */
public class ConsultationTransfer extends SupervisedTransfer {
    private static final String CONSULTATION_TRANSFER_TYPE = "consultation";

    /**
     * @param name The name of this turn. Not empty.
     * @param destination The URI of the destination (telephone, IP telephony
     *            address). Not empty.
     */
    public ConsultationTransfer(String name, String destination) {
        super(name, destination);
    }

    @Override
    protected final String getTransferType() {
        return CONSULTATION_TRANSFER_TYPE;
    }
}