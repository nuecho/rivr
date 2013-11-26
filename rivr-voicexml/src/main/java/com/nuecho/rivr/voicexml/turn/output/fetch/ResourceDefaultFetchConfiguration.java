/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.fetch;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;

/**
 * VoiceXML resource fetch configuration default properties: <code>maxage</code>
 * , <code>maxstale</code> and <code>fetchhint</code>.
 * <p>
 * This class defines default values for fetch properties and is used in
 * {@link DefaultFetchConfiguration} which is used to generate VoiceXML global
 * default properties for resource and document fetching.
 * <p>
 * To specify the fetch properties on a per-resource basis, specify a
 * {@link FetchConfiguration} on the required resource, for example
 * {@link AudioFile#setFetchConfiguration(FetchConfiguration)}.
 * 
 * @see DefaultFetchConfiguration
 * @author Nu Echo Inc.
 */
public class ResourceDefaultFetchConfiguration {

    private final ResourceType mResourceType;

    private Duration mMaxAge;
    private Duration mMaxStale;
    private FetchHint mFetchHint;

    /**
     * Types of resources that can be fetched.
     * 
     * @author Nu Echo Inc.
     */
    public enum ResourceType {
        object, audio, grammar, script
    }

    public ResourceDefaultFetchConfiguration(ResourceType resourceType) {
        Assert.notNull(resourceType, "resourceType");
        mResourceType = resourceType;
    }

    public final ResourceType getResourceType() {
        return mResourceType;
    }

    public final Duration getMaxAge() {
        return mMaxAge;
    }

    public final void setMaxAge(Duration maxAge) {
        mMaxAge = maxAge;
    }

    public final Duration getMaxStale() {
        return mMaxStale;
    }

    public final void setMaxStale(Duration maxStale) {
        mMaxStale = maxStale;
    }

    public final FetchHint getFetchHint() {
        return mFetchHint;
    }

    public final void setFetchHint(FetchHint fetchHint) {
        mFetchHint = fetchHint;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mFetchHint == null ? 0 : mFetchHint.hashCode());
        result = prime * result + (mMaxAge == null ? 0 : mMaxAge.hashCode());
        result = prime * result + (mMaxStale == null ? 0 : mMaxStale.hashCode());
        result = prime * result + (mResourceType == null ? 0 : mResourceType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ResourceDefaultFetchConfiguration other = (ResourceDefaultFetchConfiguration) obj;
        if (mFetchHint == null) {
            if (other.mFetchHint != null) return false;
        } else if (!mFetchHint.equals(other.mFetchHint)) return false;
        if (mMaxAge == null) {
            if (other.mMaxAge != null) return false;
        } else if (!mMaxAge.equals(other.mMaxAge)) return false;
        if (mMaxStale == null) {
            if (other.mMaxStale != null) return false;
        } else if (!mMaxStale.equals(other.mMaxStale)) return false;
        if (mResourceType == null) {
            if (other.mResourceType != null) return false;
        } else if (!mResourceType.equals(other.mResourceType)) return false;
        return true;
    }
}