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

    private final JsonStructure mJsonData;
    private final JsonpMode mJsonpMode;
    private final String mJsonpCallback;

    enum JsonpMode {
        DISABLED, NORMAL, TEXTAREA
    }

    public JsonServletResponseContent(JsonStructure jsonData, JsonpMode jsonpMode, String jsonpCallback) {
        Assert.notNull(jsonpMode, "jsonpMode");
        Assert.notNull(jsonData, "jsonData");
        mJsonData = jsonData;
        mJsonpMode = jsonpMode;
        mJsonpCallback = jsonpCallback;
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
        Writer writer = new OutputStreamWriter(outputStream, "utf-8");

        if (mJsonpMode == JsonpMode.TEXTAREA) {
            writer.write("<textarea>");
        }

        if (mJsonpMode != JsonpMode.DISABLED) {
            writer.write(mJsonpCallback);
            writer.write('(');
        }

        JsonUtils.write(writer, mJsonData);

        if (mJsonpMode != JsonpMode.DISABLED) {
            writer.write(')');
        }

        if (mJsonpMode == JsonpMode.TEXTAREA) {
            writer.write("</textarea>");
        }

        writer.flush();
    }

}
