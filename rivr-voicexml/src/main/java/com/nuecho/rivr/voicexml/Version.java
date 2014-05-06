/*
 * Copyright (c) 2014 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml;

import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public class Version extends VersionBase {
    public static String getVersion() {
        return new Version().getVersion(Version.class);
    }
}
