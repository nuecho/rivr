/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.audio;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A {@link SpeechSynthesis} represents a text that will be spoken using a
 * synthesis voice. It can be either a string of text or a SSML document.
 * 
 * @author Nu Echo Inc.
 * @see <a
 *      href="https://www.w3.org/TR/speech-synthesis/">https://www.w3.org/TR/speech-synthesis/</a>
 */
public final class SpeechSynthesis extends AudioItem {
    public static final String SPEECH_SYNTHESIS_ELEMENT_TYPE = "speechSynthesis";
    private static final String TEXT_PROPERTY = "text";
    private static final String SSML_PROPERTY = "ssml";

    private final String mText;
    private final DocumentFragment mDocumentFragment;

    /**
     * @param text The synthesis text. Not empty.
     */
    public SpeechSynthesis(String text) {
        Assert.notEmpty(text, "text");
        mText = text;
        mDocumentFragment = null;
    }

    /**
     * @param documentFragment The SSML document. Not null.
     */
    public SpeechSynthesis(DocumentFragment documentFragment) {
        Assert.notNull(documentFragment, "documentFragment");
        mDocumentFragment = documentFragment;
        mText = null;
    }

    @Override
    public String getElementType() {
        return SPEECH_SYNTHESIS_ELEMENT_TYPE;
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
        SpeechSynthesis other = (SpeechSynthesis) obj;
        if (mDocumentFragment == null) {
            if (other.mDocumentFragment != null) return false;
        } else if (!mDocumentFragment.equals(other.mDocumentFragment)) return false;
        if (mText == null) {
            if (other.mText != null) return false;
        } else if (!mText.equals(other.mText)) return false;
        return true;
    }
}