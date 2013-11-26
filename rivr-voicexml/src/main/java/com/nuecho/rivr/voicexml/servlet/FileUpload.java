/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.voicexml.util.json.*;

/**
 * HTTP-uploaded file via <code>multipart/form-data</code>, typically the audio
 * file of a recording. This class contains the content type, the headers and
 * the data (ie. a byte array).
 * 
 * @author Nu Echo Inc.
 */
public final class FileUpload implements JsonSerializable {

    private static final String SIZE_PROPERTY = "size";
    private static final String TYPE_PROPERTY = "type";
    private static final String FILENAME_PROPERTY = "filename";

    private final String mName;
    private final String mContentType;
    private final byte[] mContent;
    private final Map<String, String> mHeaders;

    public FileUpload(String name, String contentType, byte[] content, Map<String, String> headers) {
        mName = name;
        mContentType = contentType;
        mContent = content;
        mHeaders = headers;
    }

    public String getName() {
        return mName;
    }

    public String getContentType() {
        return mContentType;
    }

    public byte[] getContent() {
        return mContent;
    }

    public Set<String> getHeaderNames() {
        return Collections.unmodifiableSet(mHeaders.keySet());
    }

    public String getHeader(String name) {
        return mHeaders.get(name);
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, FILENAME_PROPERTY, mName);
        JsonUtils.add(builder, TYPE_PROPERTY, mContentType);
        if (mContent != null) {
            builder.add(SIZE_PROPERTY, (long) mContent.length);
        }
        //content is not serialized
        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mContentType == null) ? 0 : mContentType.hashCode());
        result = prime * result + Arrays.hashCode(mContent);
        result = prime * result + ((mHeaders == null) ? 0 : mHeaders.hashCode());
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FileUpload other = (FileUpload) obj;
        if (mContentType == null) {
            if (other.mContentType != null) return false;
        } else if (!mContentType.equals(other.mContentType)) return false;
        if (!Arrays.equals(mContent, other.mContent)) return false;
        if (mHeaders == null) {
            if (other.mHeaders != null) return false;
        } else if (!mHeaders.equals(other.mHeaders)) return false;
        if (mName == null) {
            if (other.mName != null) return false;
        } else if (!mName.equals(other.mName)) return false;
        return true;
    }
}
