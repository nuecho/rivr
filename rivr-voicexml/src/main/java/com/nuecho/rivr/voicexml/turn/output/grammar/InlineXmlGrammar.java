/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.grammar;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * An {@link InlineXmlGrammar} represents an XML source grammar inlined in the
 * VoiceXML document.
 * 
 * @author Nu Echo Inc.
 */
public final class InlineXmlGrammar extends GrammarItem {
    private static final String SOURCE_PROPERTY = "source";
    private static final String INLINE_XML_ELEMENT_TYPE = "inlineXml";

    private final Document mDocument;

    /**
     * @param document The XML source of the grammar. Not null.
     */
    public InlineXmlGrammar(Document document) {
        Assert.notNull(document, "document");
        mDocument = document;
    }

    public Document getDocument() {
        return mDocument;
    }

    @Override
    public String getElementType() {
        return INLINE_XML_ELEMENT_TYPE;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.addXmlNodeProperty(builder, SOURCE_PROPERTY, "grammar document", mDocument);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (mDocument == null ? 0 : mDocument.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        InlineXmlGrammar other = (InlineXmlGrammar) obj;
        if (mDocument == null) {
            if (other.mDocument != null) return false;
        } else if (!mDocument.equals(other.mDocument)) return false;
        return true;
    }
}