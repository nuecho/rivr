/*
 * Copyright (c) 2002-2003 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class SynthesisText extends AudioItem {

    private static final String TEXT_PROPERTY = "text";
    private static final String SSML_PROPERTY = "ssml";
    public static final String SYNTHESIS_TEXT_ELEMENT_TYPE = "synthesisText";

    private final String mText;
    private final DocumentFragment mDocumentFragment;

    public SynthesisText(String text) {
        Assert.notEmpty(text, "text");
        mText = text;
        mDocumentFragment = null;
    }

    public SynthesisText(DocumentFragment documentFragment) {
        Assert.notNull(documentFragment, "documentFragment");
        mDocumentFragment = documentFragment;
        mText = null;
    }

    @Override
    public String getElementType() {
        return SYNTHESIS_TEXT_ELEMENT_TYPE;
    }

    public String getText() {
        return mText;
    }

    public DocumentFragment getDocumentFragment() {
        return mDocumentFragment;
    }

    public boolean isSsml() {
        return mDocumentFragment != null;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {

        if (isSsml()) {
            JsonUtils.addXmlNodeProperty(builder, SSML_PROPERTY, "SSML fragment", mDocumentFragment);
        } else {
            builder.addNull(SSML_PROPERTY);
        }

        JsonUtils.add(builder, TEXT_PROPERTY, mText);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mDocumentFragment == null) ? 0 : mDocumentFragment.hashCode());
        result = prime * result + ((mText == null) ? 0 : mText.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SynthesisText other = (SynthesisText) obj;
        if (mDocumentFragment == null) {
            if (other.mDocumentFragment != null) return false;
        } else if (!mDocumentFragment.equals(other.mDocumentFragment)) return false;
        if (mText == null) {
            if (other.mText != null) return false;
        } else if (!mText.equals(other.mText)) return false;
        return true;
    }

}