/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.json;

import java.io.*;

import javax.json.*;

import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Wraps a JSON value in a {@link ServletResponseContent}.
 * 
 * @author Nu Echo Inc.
 */
public class JsonServletResponseContent implements ServletResponseContent {

    private static final String JSON_MIME_TYPE = "application/json";
    private static final String HTML_MIME_TYPE = "text/html";

    private final byte[] mContent;
    private final JsonpMode mJsonpMode;

    enum JsonpMode {
        DISABLED, NORMAL, TEXTAREA
    }

    public JsonServletResponseContent(JsonStructure jsonData, JsonpMode jsonpMode, String jsonpCallback)
            throws IOException {
        Assert.notNull(jsonpMode, "jsonpMode");
        Assert.notNull(jsonData, "jsonData");

        mJsonpMode = jsonpMode;
        mContent = buildContent(jsonData, jsonpCallback);
    }

    @Override
    public String getContentType() {
        if (mJsonpMode == JsonpMode.DISABLED) return JSON_MIME_TYPE;
        else if (mJsonpMode == JsonpMode.NORMAL) return JSON_MIME_TYPE;
        else if (mJsonpMode == JsonpMode.TEXTAREA) return HTML_MIME_TYPE;
        else throw new AssertionError("Unexpected JSONP mode: " + mJsonpMode);
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

    private byte[] buildContent(JsonStructure jsonData, String jsonpCallback) throws IOException {

        ByteArrayOutputStream binaryArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(binaryArrayOutputStream, "utf-8");

        if (mJsonpMode == JsonpMode.TEXTAREA) {
            writer.write("<textarea>");
        }

        if (mJsonpMode != JsonpMode.DISABLED) {
            writer.write(jsonpCallback);
            writer.write('(');
        }

        JsonUtils.write(writer, jsonData);

        if (mJsonpMode != JsonpMode.DISABLED) {
            writer.write(')');
        }

        if (mJsonpMode == JsonpMode.TEXTAREA) {
            writer.write("</textarea>");
        }

        writer.flush();
        return binaryArrayOutputStream.toByteArray();
    }

    /**
     * @since 1.0.2
     */
    @Override
    public Integer getContentLength() {
        return mContent.length;
    }

}
