/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.input;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * {@link TransferStatusInfo} is the result of a {@link Transfer} and is
 * composed of a {@link TransferStatus} and the transfer {@link Duration}.
 * 
 * @author Nu Echo Inc.
 * @see TransferStatus
 */
public final class TransferStatusInfo implements JsonSerializable {

    private static final String DURATION_PROPERTY = "duration";
    private static final String STATUS_PROPERTY = "status";
    private final TransferStatus mStatus;
    private final Duration mDuration;

    public TransferStatusInfo(TransferStatus status, Duration duration) {
        Assert.notNull(status, STATUS_PROPERTY);
        Assert.notNull(duration, DURATION_PROPERTY);
        mStatus = status;
        mDuration = duration;
    }

    public TransferStatus getStatus() {
        return mStatus;
    }

    public Duration getDuration() {
        return mDuration;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, STATUS_PROPERTY, mStatus.asJson());
        builder.add(DURATION_PROPERTY, mDuration.getMilliseconds());
        return builder.build();
    }
}