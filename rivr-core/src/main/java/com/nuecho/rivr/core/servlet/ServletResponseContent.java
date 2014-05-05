/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import java.io.*;

/**
 * Encapsulates servlet response (content, type and length)
 * 
 * @author Nu Echo Inc.
 */
public interface ServletResponseContent {
    String getContentType();

    void writeTo(OutputStream outputStream) throws IOException;

    /**
     * @since 1.0.2
     */
    Integer getContentLength();
}