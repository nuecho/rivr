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
 * @author Nu Echo Inc.
 */
public class BridgeTransferTurn extends SupervisedTransferTurn {

    private static final String MAXIMUM_TIME_PROPERTY_NAME = "maximumTime";
    public static final String TYPE = "bridge";

    private TimeValue mMaximumTime;

    public BridgeTransferTurn(String name, String destination) {
        super(name, destination);
    }

    public TimeValue getMaximumTime() {
        return mMaximumTime;
    }

    public void setMaximumTime(TimeValue maximumTime) {
        mMaximumTime = maximumTime;
    }

    @Override
    public String getTransferType() {
        return TYPE;
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