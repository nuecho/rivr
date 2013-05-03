/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.util;

import java.io.*;

import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class IOUtils {

    private static final int BUFFER_SIZE = 4096;

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        Assert.notNull(inputStream, "inputStream");
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream bufferedInputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int length = 0;
        while ((length = bufferedInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
            outputStream.write(buffer, 0, length);
        }

        return outputStream.toByteArray();
    }

    private IOUtils() {}
}