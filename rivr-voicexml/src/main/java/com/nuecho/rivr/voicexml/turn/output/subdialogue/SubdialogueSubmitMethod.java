/*
 * Copyright (c) 2002-2005 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.subdialogue;

import java.util.*;

/**
 * @author NuEcho Inc.
 */
public final class SubdialogueSubmitMethod {
    private static Map<String, SubdialogueSubmitMethod> sInstances = new HashMap<String, SubdialogueSubmitMethod>();

    public static final SubdialogueSubmitMethod GET = new SubdialogueSubmitMethod("get");
    public static final SubdialogueSubmitMethod POST = new SubdialogueSubmitMethod("post");

    private final String mKey;

    private SubdialogueSubmitMethod(String key) {
        mKey = key;
        sInstances.put(key, this);
    }

    public boolean isInternal() {
        return false;
    }

    public static SubdialogueSubmitMethod getMethodByName(String key) {
        return sInstances.get(key);
    }

    public static boolean exists(String key) {
        return sInstances.containsKey(key);
    }

    @Override
    public String toString() {
        return mKey;
    }

    public String getKey() {
        return mKey;
    }

    public Object readResolve() {
        return getMethodByName(mKey);
    }
}