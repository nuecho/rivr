/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.grammar;

import javax.json.*;

import com.nuecho.rivr.voicexml.util.json.*;

/**
 * This abstract class is the superclass of all classes representing a grammar
 * element in the dialogue. It is not intended to be subclassed.
 * 
 * @author Nu Echo Inc.
 * @see GrammarReference
 * @see InlineStringGrammar
 * @see InlineXmlGrammar
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml3.1">http://www.w3.org/TR/voicexml20/#dml3.1</a>
 */
public abstract class GrammarItem implements JsonSerializable {
    public static final String TYPE_PROPERTY = "type";

    private Double mWeight;
    private String mMediaType;

    /**
     * @param weight The relative weight of this grammar. <code>null</code> to
     *            use the VoiceXML platform default
     * @see <a
     *      href="http://www.w3.org/TR/voicexml20/#dml3.1.1.3">http://www.w3.org/TR/voicexml20/#dml3.1.1.3</a>
     */
    public final void setWeight(Double weight) {
        mWeight = weight;
    }

    /**
     * @param mediaType The preferred media type for this grammar.
     *            <code>null</code> to use the VoiceXML platform default
     * @see <a
     *      href="http://www.w3.org/TR/voicexml20/#dml3.1.1.4">http://www.w3.org/TR/voicexml20/#dml3.1.1.4</a>
     */
    public final void setMediaType(String mediaType) {
        mMediaType = mediaType;
    }

    public final Double getWeight() {
        return mWeight;
    }

    public final String getMediaType() {
        return mMediaType;
    }

    public abstract String getElementType();

    protected abstract void addJsonProperties(JsonObjectBuilder builder);

    @Override
    public final String toString() {
        return asJson().toString();
    }

    @Override
    public final JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, TYPE_PROPERTY, getElementType());
        addJsonProperties(builder);
        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mMediaType == null ? 0 : mMediaType.hashCode());
        result = prime * result + (mWeight == null ? 0 : mWeight.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GrammarItem other = (GrammarItem) obj;
        if (mMediaType == null) {
            if (other.mMediaType != null) return false;
        } else if (!mMediaType.equals(other.mMediaType)) return false;
        if (mWeight == null) {
            if (other.mWeight != null) return false;
        } else if (!mWeight.equals(other.mWeight)) return false;
        return true;
    }
}