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

    /**
     * Get the content type of the servlet response.
     *
     * @return the content type.
     */
    String getContentType();

    /**
     * Write the response to a stream.
     *
     * @param outputStream the destination output stream.
     * @throws IOException if an error occurred while writing the response to
     *             the stream.
     */
    void writeTo(OutputStream outputStream) throws IOException;

    /**
     * Get the string representation of the response.
     *
     * @return the string representation of the response.
     * @since 1.0.3
     */
    String getContentAsString();

    /**
     * Get the size of the response.
     *
     * @return size, in bytes, of the response.
     * @since 1.0.2
     */
    Integer getContentLength();

}