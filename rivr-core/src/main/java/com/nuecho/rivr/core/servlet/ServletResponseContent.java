/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet;

import java.io.*;

/**
 * Encapsulate servlet response (content + content type)
 * 
 * @author Nu Echo Inc.
 */
public interface ServletResponseContent {
    String getContentType();

    void writeTo(OutputStream outputStream) throws IOException;

    Integer getContentLength();
}