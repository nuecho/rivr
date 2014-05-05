/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.voicexml;

import static com.nuecho.rivr.core.util.DomUtils.*;

import java.io.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.servlet.*;

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
        writeToOutputStream(document, byteArrayOutputStream);
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

    /**
     * @since 1.0.2
     */
    @Override
    public Integer getContentLength() {
        return mContent.length;
    }
}