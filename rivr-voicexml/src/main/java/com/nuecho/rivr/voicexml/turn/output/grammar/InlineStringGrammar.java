/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.grammar;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * An {@link InlineStringGrammar} represents a text source grammar inlined in
 * the VoiceXML document.
 * 
 * @author Nu Echo Inc.
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml3.1.1.4">http://www.w3.org/TR/voicexml20/#dml3.1.1.4</a>
 */
public final class InlineStringGrammar extends GrammarItem {
    private static final String INLINE_STRING_ELEMENT_TYPE = "inlineString";
    private static final String VERSION_PROPERTY = "version";
    private static final String TAG_FORMAT_PROPERTY = "tagFormat";
    private static final String SOURCE_PROPERTY = "source";
    private static final String ROOT_PROPERTY = "root";
    private static final String LANGUAGE_PROPERTY = "language";
    private static final String BASE_PROPERTY = "base";

    private final String mSource;
    private String mVersion;
    private String mLanguage;
    private String mRoot;
    private String mTagFormat;
    private String mBase;

    /**
     * @param source The text source of the grammar. Not null.
     */
    public InlineStringGrammar(String source) {
        Assert.notNull(source, "source");
        mSource = source;
    }

    /**
     * @param version The version of the grammar.<code>null</code> to use the
     *            VoiceXML platform default.
     */
    public void setVersion(String version) {
        mVersion = version;
    }

    /**
     * @param language The language identifier of the grammar. For example,
     *            "fr-CA" for Canadian French. <code>null</code> to use the
     *            VoiceXML platform default.
     */
    public void setLanguage(String language) {
        mLanguage = language;
    }

    /**
     * @param root The name of the rule that will act as the root rule.
     *            <code>null</code> to use the VoiceXML platform default.
     */
    public void setRoot(String root) {
        mRoot = root;
    }

    /**
     * @param tagFormat The tag content format for all tags within the grammar.
     *            <code>null</code> to use the VoiceXML platform default
     */
    public void setTagFormat(String tagFormat) {
        mTagFormat = tagFormat;
    }

    /**
     * @param base The base URI from which relative URIs in the grammar are
     *            resolved. <code>null</code> to use the VoiceXML platform
     *            default
     */
    public void setBase(String base) {
        mBase = base;
    }

    @Override
    public String getElementType() {
        return INLINE_STRING_ELEMENT_TYPE;
    }

    public String getSource() {
        return mSource;
    }

    public String getVersion() {
        return mVersion;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getRoot() {
        return mRoot;
    }

    public String getTagFormat() {
        return mTagFormat;
    }

    public String getBase() {
        return mBase;
    }

    @Override
    protected void addJsonProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, BASE_PROPERTY, mBase);
        JsonUtils.add(builder, LANGUAGE_PROPERTY, mLanguage);
        JsonUtils.add(builder, ROOT_PROPERTY, mRoot);
        JsonUtils.add(builder, SOURCE_PROPERTY, mSource);
        JsonUtils.add(builder, TAG_FORMAT_PROPERTY, mTagFormat);
        JsonUtils.add(builder, VERSION_PROPERTY, mVersion);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (mBase == null ? 0 : mBase.hashCode());
        result = prime * result + (mLanguage == null ? 0 : mLanguage.hashCode());
        result = prime * result + (mRoot == null ? 0 : mRoot.hashCode());
        result = prime * result + (mSource == null ? 0 : mSource.hashCode());
        result = prime * result + (mTagFormat == null ? 0 : mTagFormat.hashCode());
        result = prime * result + (mVersion == null ? 0 : mVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        InlineStringGrammar other = (InlineStringGrammar) obj;
        if (mBase == null) {
            if (other.mBase != null) return false;
        } else if (!mBase.equals(other.mBase)) return false;
        if (mLanguage == null) {
            if (other.mLanguage != null) return false;
        } else if (!mLanguage.equals(other.mLanguage)) return false;
        if (mRoot == null) {
            if (other.mRoot != null) return false;
        } else if (!mRoot.equals(other.mRoot)) return false;
        if (mSource == null) {
            if (other.mSource != null) return false;
        } else if (!mSource.equals(other.mSource)) return false;
        if (mTagFormat == null) {
            if (other.mTagFormat != null) return false;
        } else if (!mTagFormat.equals(other.mTagFormat)) return false;
        if (mVersion == null) {
            if (other.mVersion != null) return false;
        } else if (!mVersion.equals(other.mVersion)) return false;
        return true;
    }
}