/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.input;

import javax.json.*;

import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author NuEcho Inc.
 */
public class TransferStatus implements JsonSerializable {

    public static final String NO_ANSWER = "noanswer";
    public static final String NEAR_END_DISCONNECT = "near_end_disconnect";
    public static final String FAR_END_DISCONNECT = "far_end_disconnect";
    public static final String NETWORK_DISONNECT = "network_disconnect";
    public static final String MAXTIME_DISCONNECT = "maxtime_disconnect";
    public static final String BUSY = "busy";
    public static final String NETWORK_BUSY = "network_busy";
    public static final String UNKNOWN = "unknown";

    private final String mStatusCode;

    public TransferStatus(String statusCode) {
        mStatusCode = statusCode;
    }

    public String getStatusCode() {
        return mStatusCode;
    }

    @Override
    public JsonValue asJson() {
        return JsonUtils.wrap(mStatusCode);
    }

}