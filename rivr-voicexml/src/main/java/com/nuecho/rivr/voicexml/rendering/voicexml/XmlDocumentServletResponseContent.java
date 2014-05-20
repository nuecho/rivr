/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.voicexml;

import static com.nuecho.rivr.core.util.DomUtils.*;

import java.io.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;

/**
 * {@link ServletResponseContent} wrapping a {@link Document org.w3c.Document}.
 * 
 * @author Nu Echo Inc.
 */
public class XmlDocumentServletResponseContent implements ServletResponseContent {

    private final byte[] mContent;
    private final String mContentType;

    public XmlDocumentServletResponseContent(Document document, String contentType) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeToOutputStream(document, byteArrayOutputStream, Encoding.UTF_8);
        mContent = byteArrayOutputStream.toByteArray();
        mContentType = contentType;
    }

    @Override
    public String getContentType() {
        return mContentType;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(mContent);
    }

    @Override
    public String getContentAsString() {
        try {
            return new String(mContent, Encoding.UTF_8.getId());
        } catch (UnsupportedEncodingException exception) {
            throw new AssertionError("Missing " + Encoding.UTF_8.getId() + " encoding.");
        }
    }

    /**
     * @since 1.0.2
     */
    @Override
    public Integer getContentLength() {
        return mContent.length;
    }
}