/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.core.util.Assert.*;

import java.util.*;
import java.util.Map.Entry;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.grammar.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Base class of an interaction phase recognition configuration parts.
 * 
 * @author Nu Echo Inc.
 * @see DtmfRecognition
 * @see SpeechRecognition
 */
public abstract class Recognition implements JsonSerializable {
    private static final String GRAMMARS_PROPERTY = "grammars";
    private static final String PROPERTIES_PROPERTY = "properties";

    private List<GrammarItem> mGrammarItems;
    private final Map<String, String> mProperties = new HashMap<String, String>();

    /**
     * @param grammarItems The list of {@link GrammarItem}. Not null.
     */
    Recognition(List<? extends GrammarItem> grammarItems) {
        setGrammarItems(grammarItems);
    }

    /**
     * @param grammarItems The list of {@link GrammarItem}. Not null.
     */
    Recognition(GrammarItem... grammarItems) {
        setGrammarItems(grammarItems);
    }

    /**
     * Adds a property to the enclosing form
     * 
     * @param propertyName The name of the property. Not empty.
     * @param propertyValue The value of the property. Not null.
     * @see <a
     *      href="http://www.w3.org/TR/voicexml20/#dml6.3">http://www.w3.org/TR/voicexml20/#dml6.3</a>
     */
    public void addProperty(String propertyName, String propertyValue) {
        Assert.notEmpty(propertyName, "propertyName");
        Assert.notNull(propertyValue, "propertyValue");
        mProperties.put(propertyName, propertyValue);
    }

    public void removeProperty(String propertyName) {
        mProperties.remove(propertyName);
    }

    public List<GrammarItem> getGrammarItems() {
        return Collections.unmodifiableList(mGrammarItems);
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

    protected final void copyPropertiesTo(Recognition copy) {
        for (String propertyName : getPropertyNames()) {
            copy.addProperty(propertyName, getProperty(propertyName));
        }
    }

    public void setGrammarItems(GrammarItem... grammarItems) {
        setGrammarItems(asListChecked(grammarItems));
    }

    public void setGrammarItems(List<? extends GrammarItem> grammarItems) {
        Assert.noNullValues(grammarItems, "grammarItems");
        mGrammarItems = new ArrayList<GrammarItem>(grammarItems);
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
        Recognition other = (Recognition) obj;
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