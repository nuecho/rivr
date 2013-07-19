/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.transfer;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>BridgeTransferTurn</code> is a {@link SupervisedTransferTurn} that
 * connects the caller to the callee in a full duplex conversation.
 * 
 * @author Nu Echo Inc.
 * @see http://www.w3.org/TR/voicexml20/#dml2.3.7.2
 */
public class BridgeTransferTurn extends SupervisedTransferTurn {
    private static final String BRIDGE_TRANSFER_TYPE = "bridge";

    private static final String MAXIMUM_TIME_PROPERTY_NAME = "maximumTime";

    private TimeValue mMaximumTime;

    /**
     * @param name The name of this turn. Not empty.
     * @param destination The URI of the destination (telephone, IP telephony
     *            address). Not empty.
     */
    public BridgeTransferTurn(String name, String destination) {
        super(name, destination);
    }

    /**
     * @param maximumTime The time that the call is allowed to last. Null
     *            reverts to VoiceXML default value.
     */
    public final void setMaximumTime(TimeValue maximumTime) {
        mMaximumTime = maximumTime;
    }

    public final TimeValue getMaximumTime() {
        return mMaximumTime;
    }

    @Override
    protected final String getTransferType() {
        return BRIDGE_TRANSFER_TYPE;
    }

    @Override
    protected void customizeTransferElement(Element transferElement) throws VoiceXmlDocumentRenderingException {
        setTimeAttribute(transferElement, MAXTIME_ATTRIBUTE, mMaximumTime);
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        super.addJsonProperties(builder);
        JsonUtils.addTimeProperty(builder, MAXIMUM_TIME_PROPERTY_NAME, mMaximumTime);
    }
}