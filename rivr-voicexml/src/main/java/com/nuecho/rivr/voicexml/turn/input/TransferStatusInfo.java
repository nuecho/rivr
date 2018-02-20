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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mDuration == null) ? 0 : mDuration.hashCode());
        result = prime * result + ((mStatus == null) ? 0 : mStatus.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TransferStatusInfo other = (TransferStatusInfo) obj;
        if (mDuration == null) {
            if (other.mDuration != null) return false;
        } else if (!mDuration.equals(other.mDuration)) return false;
        if (mStatus == null) {
            if (other.mStatus != null) return false;
        } else if (!mStatus.equals(other.mStatus)) return false;
        return true;
    }

}