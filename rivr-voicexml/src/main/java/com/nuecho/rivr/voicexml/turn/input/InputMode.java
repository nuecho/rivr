/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.input;

import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public enum InputMode {
    DTMF("dtmf"), VOICE("voice");

    public static InputMode getInputModeByKey(String key) {
        Assert.notNull(key, "key");

        key = key.toLowerCase();

        if (key.equals("dtmf")) return DTMF;
        else if (key.equals("voice")) return VOICE;
        else return null;
    }

    private final String mKey;

    InputMode(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }
}