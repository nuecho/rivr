/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import static java.util.Arrays.*;

import java.util.*;
import java.util.Map.Entry;

import javax.json.*;

import com.nuecho.rivr.voicexml.turn.output.grammar.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class RecognitionConfiguration implements JsonSerializable {

    private static final String GRAMMARS_PROPERTY = "grammars";
    private static final String PROPERTIES_PROPERTY = "properties";

    private List<GrammarItem> mGrammarItems;
    private final Map<String, String> mProperties = new HashMap<String, String>();

    public List<GrammarItem> getGrammarItems() {
        return Collections.unmodifiableList(mGrammarItems);
    }

    public void setGrammarItems(GrammarItem... grammarItems) {
        setGrammarItems(asList(grammarItems));
    }

    public void setGrammarItems(List<? extends GrammarItem> grammarItems) {
        mGrammarItems = new ArrayList<GrammarItem>(grammarItems);
    }

    public void addProperty(String propertyName, String propertyValue) {
        mProperties.put(propertyName, propertyValue);
    }

    public void removeProperty(String propertyName) {
        mProperties.remove(propertyName);
    }

    public boolean hasProperty(String propertyName) {
        return mProperties.containsKey(propertyName);
    }

    public Set<String> getPropertyNames() {
        return Collections.unmodifiableSet(mProperties.keySet());
    }

    public String getProperty(String propertyName) {
        return mProperties.get(propertyName);
    }

    protected final void copyPropertiesTo(RecognitionConfiguration copy) {
        for (String propertyName : getPropertyNames()) {
            copy.addProperty(propertyName, getProperty(propertyName));
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mGrammarItems == null) ? 0 : mGrammarItems.hashCode());
        result = prime * result + ((mProperties == null) ? 0 : mProperties.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RecognitionConfiguration other = (RecognitionConfiguration) obj;
        if (mGrammarItems == null) {
            if (other.mGrammarItems != null) return false;
        } else if (!mGrammarItems.equals(other.mGrammarItems)) return false;
        if (mProperties == null) {
            if (other.mProperties != null) return false;
        } else if (!mProperties.equals(other.mProperties)) return false;
        return true;
    }

    @Override
    public final String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();

        JsonObjectBuilder propertiesBuilder = JsonUtils.createObjectBuilder();
        for (Entry<String, String> entry : mProperties.entrySet()) {
            JsonUtils.add(propertiesBuilder, entry.getKey(), entry.getValue());
        }
        JsonUtils.add(builder, PROPERTIES_PROPERTY, propertiesBuilder);
        JsonUtils.add(builder, GRAMMARS_PROPERTY, JsonUtils.toJson(mGrammarItems));
        addJsonProperties(builder);
        return builder.build();
    }

    protected abstract void addJsonProperties(JsonObjectBuilder builder);
}