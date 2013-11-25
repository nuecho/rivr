/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A {@link BridgeTransfer} is a {@link SupervisedTransfer} that connects the
 * caller to the callee in a full duplex conversation.
 * 
 * @author Nu Echo Inc.
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml2.3.7.2">http://www.w3.org/TR/voicexml20/#dml2.3.7.2</a>
 */
public class BridgeTransfer extends SupervisedTransfer {
    private static final String BRIDGE_TRANSFER_TYPE = "bridge";

    private static final String MAXIMUM_TIME_PROPERTY_NAME = "maximumTime";

    private Duration mMaximumTime;

    /**
     * @param name The name of this turn. Not empty.
     * @param destination The URI of the destination (telephone, IP telephony
     *            address). Not empty.
     */
    public BridgeTransfer(String name, String destination) {
        super(name, destination);
    }

    /**
     * @param maximumTime The time that the call is allowed to last.
     *            <code>null</code> to use the VoiceXML platform default.
     */
    public final void setMaximumTime(Duration maximumTime) {
        mMaximumTime = maximumTime;
    }

    public final Duration getMaximumTime() {
        return mMaximumTime;
    }

    @Override
    protected final String getTransferType() {
        return BRIDGE_TRANSFER_TYPE;
    }

    @Override
    protected void customizeTransferElement(Element transferElement) throws VoiceXmlDocumentRenderingException {
        setDurationAttribute(transferElement, MAXTIME_ATTRIBUTE, mMaximumTime);
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        super.addTurnProperties(builder);
        JsonUtils.addDurationProperty(builder, MAXIMUM_TIME_PROPERTY_NAME, mMaximumTime);
    }

    /**
     * Builder used to ease the creation of instances of {@link BridgeTransfer}.
     */
    public static class Builder extends SupervisedTransfer.Builder {

        private Duration mMaximumTime;

        public Builder(String name) {
            super(name);
        }

        public Builder setMaximumDuration(Duration maximumTime) {
            mMaximumTime = maximumTime;
            return this;
        }

        public BridgeTransfer build() {
            BridgeTransfer bridgeTransfer = new BridgeTransfer(getName(), getDestination());
            bridgeTransfer.setMaximumTime(mMaximumTime);
            super.build(bridgeTransfer);
            return bridgeTransfer;

        }
    }
}