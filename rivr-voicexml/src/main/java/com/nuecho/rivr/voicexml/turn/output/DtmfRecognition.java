/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.grammar.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A {@link DtmfRecognition} represents the DTMF part of an interaction phase
 * recognition configuration. It is composed of a list of DTMF
 * {@link GrammarItem} and optional properties (inter-digit timeout, term
 * timeout, term char).
 * 
 * @author Nu Echo Inc.
 */
public final class DtmfRecognition extends Recognition {
    private static final String INTERDIGIT_TIMEOUT_PROPERTY = "interdigitTimeout";
    private static final String TERM_TIMEOUT_PROPERTY = "termTimeout";
    private static final String TERM_CHAR_PROPERTY = "termChar";

    private Duration mInterDigitTimeout;
    private Duration mTermTimeout;
    private String mTermChar;

    /**
     * @param grammarItems The list of DTMF {@link GrammarItem}. Not null.
     */
    public DtmfRecognition(GrammarItem... grammarItems) {
        super(grammarItems);
    }

    /**
     * @param grammarItems The list of DTMF {@link GrammarItem}. Not null.
     */
    public DtmfRecognition(List<GrammarItem> grammarItems) {
        super(grammarItems);
    }

    /**
     * @param interDigitTimeout The inter-digit timeout value to use when
     *            recognizing DTMF input. <code>null</code> to use the VoiceXML
     *            platform default.
     * @see <a
     *      href="https://www.w3.org/TR/voicexml20/#dml6.3.3">https://www.w3.org/TR/voicexml20/#dml6.3.3</a>
     */
    public void setInterDigitTimeout(Duration interDigitTimeout) {
        mInterDigitTimeout = interDigitTimeout;
    }

    /**
     * @param termTimeout The terminating timeout to use when recognizing DTMF
     *            input. <code>null</code> to use the VoiceXML platform default
     * @see <a
     *      href="https://www.w3.org/TR/voicexml20/#dml6.3.3">https://www.w3.org/TR/voicexml20/#dml6.3.3</a>
     */
    public void setTermTimeout(Duration termTimeout) {
        mTermTimeout = termTimeout;
    }

    /**
     * @param termChar The terminating DTMF character for DTMF input
     *            recognition. Must be empty or a single character.
     *            <code>null</code> to use the VoiceXML platform default
     * @see <a
     *      href="https://www.w3.org/TR/voicexml20/#dml6.3.3">https://www.w3.org/TR/voicexml20/#dml6.3.3</a>
     */
    public void setTermChar(String termChar) {
        if (termChar != null) {
            Assert.ensure(termChar.equals("") || termChar.length() == 1, "Termchar must be empty or single character");
        }

        mTermChar = termChar;
    }

    public Duration getInterDigitTimeout() {
        return mInterDigitTimeout;
    }

    public Duration getTermTimeout() {
        return mTermTimeout;
    }

    public String getTermChar() {
        return mTermChar;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.addDurationProperty(builder, INTERDIGIT_TIMEOUT_PROPERTY, mInterDigitTimeout);
        JsonUtils.addDurationProperty(builder, TERM_TIMEOUT_PROPERTY, mTermTimeout);
        JsonUtils.add(builder, TERM_CHAR_PROPERTY, mTermChar);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (mInterDigitTimeout == null ? 0 : mInterDigitTimeout.hashCode());
        result = prime * result + (mTermChar == null ? 0 : mTermChar.hashCode());
        result = prime * result + (mTermTimeout == null ? 0 : mTermTimeout.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        DtmfRecognition other = (DtmfRecognition) obj;
        if (mInterDigitTimeout == null) {
            if (other.mInterDigitTimeout != null) return false;
        } else if (!mInterDigitTimeout.equals(other.mInterDigitTimeout)) return false;
        if (mTermChar == null) {
            if (other.mTermChar != null) return false;
        } else if (!mTermChar.equals(other.mTermChar)) return false;
        if (mTermTimeout == null) {
            if (other.mTermTimeout != null) return false;
        } else if (!mTermTimeout.equals(other.mTermTimeout)) return false;
        return true;
    }

    public DtmfRecognition copy() {
        DtmfRecognition copy = new DtmfRecognition(getGrammarItems());
        copy.mInterDigitTimeout = mInterDigitTimeout;
        copy.mTermChar = mTermChar;
        copy.mTermTimeout = mTermTimeout;
        copyPropertiesTo(copy);
        return copy;
    }
}