/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.grammar;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A {@link GrammarReference} represents an external grammar reachable with
 * an URI.
 * 
 * @author Nu Echo Inc.
 * @see <a href="http://www.w3.org/TR/voicexml20/#dml3.1.1.2">http://www.w3.org/TR/voicexml20/#dml3.1.1.2</a>
 */
public final class GrammarReference extends GrammarItem {
    private static final String REFERENCE_ELEMENT_TYPE = "reference";
    private static final String URI_PROPERTY = "uri";
    private static final String RESOURCE_FETCH_CONFIGURATION_PROPERTY = "resourceFetchConfiguration";

    private final String mUri;
    private ResourceFetchConfiguration mResourceFetchConfiguration;

    /**
     * @param uri The URI of the external grammar. Not empty.
     */
    public GrammarReference(String uri) {
        Assert.notEmpty(uri, "uri");
        mUri = uri;
    }

    /**
     * @param resourceFetchConfiguration The resource fetch configuration. <code>null</code> to use the VoiceXML platform default.
     */
    public void setResourceFetchConfiguration(ResourceFetchConfiguration resourceFetchConfiguration) {
        mResourceFetchConfiguration = resourceFetchConfiguration;
    }

    @Override
    public String getElementType() {
        return REFERENCE_ELEMENT_TYPE;
    }

    public String getUri() {
        return mUri;
    }

    public ResourceFetchConfiguration getResourceFetchConfiguration() {
        return mResourceFetchConfiguration;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, URI_PROPERTY, mUri);
        JsonUtils.add(builder, RESOURCE_FETCH_CONFIGURATION_PROPERTY, mResourceFetchConfiguration);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mResourceFetchConfiguration == null ? 0 : mResourceFetchConfiguration.hashCode());
        result = prime * result + (mUri == null ? 0 : mUri.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GrammarReference other = (GrammarReference) obj;
        if (mResourceFetchConfiguration == null) {
            if (other.mResourceFetchConfiguration != null) return false;
        } else if (!mResourceFetchConfiguration.equals(other.mResourceFetchConfiguration)) return false;
        if (mUri == null) {
            if (other.mUri != null) return false;
        } else if (!mUri.equals(other.mUri)) return false;
        return true;
    }
}