/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.voicexml;

/**
 * @author Nu Echo Inc.
 */
public class VoiceXmlDocumentRenderingException extends Exception {

    private static final long serialVersionUID = 1L;

    public VoiceXmlDocumentRenderingException() {}

    public VoiceXmlDocumentRenderingException(String message) {
        super(message);
    }

    public VoiceXmlDocumentRenderingException(Throwable cause) {
        super(cause);
    }

    public VoiceXmlDocumentRenderingException(String message, Throwable cause) {
        super(message, cause);
    }

}
