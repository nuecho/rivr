/*
 * Copyright (c) 2014 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

import java.io.*;
import java.util.*;

/**
 * @author Nu Echo Inc.
 * @since 1.0.3
 */
public class VersionBase {

    private static final String VERSION_PROPERTY = "version";
    private static final String VERSION_PROPERTIES_RESOURCE_NAME = "version.properties";

    protected String getVersion(Class<? extends VersionBase> versionClass) {
        try {
            Properties versionProperties = new Properties();
            InputStream versionPropertyFile = versionClass.getResourceAsStream(VERSION_PROPERTIES_RESOURCE_NAME);
            versionProperties.load(versionPropertyFile);
            String version = versionProperties.getProperty(VERSION_PROPERTY);
            return version == null ? "" : version;
        } catch (IOException exception) {
            return "";
        }
    }

}
