/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.fetch;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class ResourceFetchConfiguration implements JsonSerializable {

    private static final String MAX_AGE_PROPERTY = "maxAge";
    private static final String MAX_STALE_PROPERTY = "maxStale";
    private static final String TIME_OUT_PROPERTY = "timeOut";
    private static final String FETCH_HINT_PROPERTY = "fetchHint";

    private Duration mTimeOut;
    private Duration mMaxAge;
    private Duration mMaxStale;
    private FetchHint mFetchHint;

    public Duration getTimeOut() {
        return mTimeOut;
    }

    public Duration getMaxAge() {
        return mMaxAge;
    }

    public Duration getMaxStale() {
        return mMaxStale;
    }

    public FetchHint getFetchHint() {
        return mFetchHint;
    }

    public void setTimeOut(Duration timeOut) {
        mTimeOut = timeOut;
    }

    public void setMaxAge(Duration maxAge) {
        mMaxAge = maxAge;
    }

    public void setMaxStale(Duration maxStale) {
        mMaxStale = maxStale;
    }

    public void setFetchHint(FetchHint fetchHint) {
        mFetchHint = fetchHint;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.addDurationProperty(builder, MAX_AGE_PROPERTY, mMaxAge);
        JsonUtils.addDurationProperty(builder, MAX_STALE_PROPERTY, mMaxStale);
        JsonUtils.addDurationProperty(builder, TIME_OUT_PROPERTY, mTimeOut);

        if (mFetchHint == null) {
            builder.addNull(FETCH_HINT_PROPERTY);
        } else {
            JsonUtils.add(builder, FETCH_HINT_PROPERTY, mFetchHint.name());
        }

        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mFetchHint == null ? 0 : mFetchHint.hashCode());
        result = prime * result + (mMaxAge == null ? 0 : mMaxAge.hashCode());
        result = prime * result + (mMaxStale == null ? 0 : mMaxStale.hashCode());
        result = prime * result + (mTimeOut == null ? 0 : mTimeOut.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ResourceFetchConfiguration other = (ResourceFetchConfiguration) obj;
        if (mFetchHint == null) {
            if (other.mFetchHint != null) return false;
        } else if (!mFetchHint.equals(other.mFetchHint)) return false;
        if (mMaxAge == null) {
            if (other.mMaxAge != null) return false;
        } else if (!mMaxAge.equals(other.mMaxAge)) return false;
        if (mMaxStale == null) {
            if (other.mMaxStale != null) return false;
        } else if (!mMaxStale.equals(other.mMaxStale)) return false;
        if (mTimeOut == null) {
            if (other.mTimeOut != null) return false;
        } else if (!mTimeOut.equals(other.mTimeOut)) return false;
        return true;
    }

}