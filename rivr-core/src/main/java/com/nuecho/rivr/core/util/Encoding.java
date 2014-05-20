/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

/**
 * Some predefined encodings.
 * 
 * @author Nu Echo Inc.
 */
public enum Encoding {
    US_ASCII("US-ASCII"), UTF_8("UTF-8"), ISO_8859_1("ISO-8859-1");

    private String mId;

    private Encoding(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }
}