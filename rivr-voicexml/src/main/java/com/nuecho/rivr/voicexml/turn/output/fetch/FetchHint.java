/*
 * Copyright (c) 2002-2005 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.fetch;

/**
 * @author NuEcho Inc.
 */
public enum FetchHint {
    PREFETCH("prefetch"), SAFE("safe");

    private final String mKey;

    private FetchHint(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    @Override
    public String toString() {
        return mKey;
    }
}