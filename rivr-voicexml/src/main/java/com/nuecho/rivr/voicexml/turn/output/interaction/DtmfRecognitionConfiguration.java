/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.grammar.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class DtmfRecognitionConfiguration extends RecognitionConfiguration {

    private static final String INTERDIGIT_TIMEOUT_PROPERTY = "interdigitTimeout";
    private static final String TERM_TIMEOUT_PROPERTY = "termTimeout";
    private static final String TERM_CHAR_PROPERTY = "termChar";

    private TimeValue mInterDigitTimeout;
    private TimeValue mTermTimeout;
    private String mTermChar;

    public DtmfRecognitionConfiguration(GrammarItem... grammarItems) {
        setGrammarItems(grammarItems);
    }

    public DtmfRecognitionConfiguration(List<GrammarItem> grammarItems) {
        setGrammarItems(grammarItems);
    }

    public void setInterDigitTimeout(TimeValue interDigitTimeout) {
        mInterDigitTimeout = interDigitTimeout;
    }

    public void setTermTimeout(TimeValue termTimeout) {
        mTermTimeout = termTimeout;
    }

    public void setTermChar(String termChar) {
        if (termChar != null) {
            Assert.ensure(termChar.equals("") || termChar.length() == 1, "Termchar must be empty or single character");
        }

        mTermChar = termChar;
    }

    public TimeValue getInterDigitTimeout() {
        return mInterDigitTimeout;
    }

    public TimeValue getTermTimeout() {
        return mTermTimeout;
    }

    public String getTermChar() {
        return mTermChar;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.addTimeProperty(builder, INTERDIGIT_TIMEOUT_PROPERTY, mInterDigitTimeout);
        JsonUtils.addTimeProperty(builder, TERM_TIMEOUT_PROPERTY, mTermTimeout);
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
        DtmfRecognitionConfiguration other = (DtmfRecognitionConfiguration) obj;
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

    public DtmfRecognitionConfiguration copy() {
        DtmfRecognitionConfiguration copy = new DtmfRecognitionConfiguration(getGrammarItems());
        copy.mInterDigitTimeout = mInterDigitTimeout;
        copy.mTermChar = mTermChar;
        copy.mTermTimeout = mTermTimeout;
        copyPropertiesTo(copy);
        return copy;
    }
}