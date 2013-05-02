/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import java.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class FileUpload {

    private final String mName;
    private final String mContentType;
    private final byte[] mData;
    private final Map<String, String> mHeaders;

    public FileUpload(String name, String contentType, byte[] data, Map<String, String> headers) {
        mName = name;
        mContentType = contentType;
        mData = data;
        mHeaders = headers;
    }

    public String getName() {
        return mName;
    }

    public String getContentType() {
        return mContentType;
    }

    public byte[] getData() {
        return mData;
    }

    public Set<String> getHeaderNames() {
        return Collections.unmodifiableSet(mHeaders.keySet());
    }

    public String getHeader(String name) {
        return mHeaders.get(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mContentType == null) ? 0 : mContentType.hashCode());
        result = prime * result + Arrays.hashCode(mData);
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
        if (!Arrays.equals(mData, other.mData)) return false;
        if (mHeaders == null) {
            if (other.mHeaders != null) return false;
        } else if (!mHeaders.equals(other.mHeaders)) return false;
        if (mName == null) {
            if (other.mName != null) return false;
        } else if (!mName.equals(other.mName)) return false;
        return true;
    }
}
