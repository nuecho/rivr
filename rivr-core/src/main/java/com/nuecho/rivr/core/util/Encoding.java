/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

/**
 * Some predefined encodings.
 * 
 * @author Nu Echo Inc.
 */
public enum Encoding {
    US_ASCII("us-ascii"), UTF_8("utf-8"), ISO_8859_1("iso-8859-1");

    private String mId;

    private Encoding(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }
}