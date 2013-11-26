/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.voicexml;

/**
 * Error during generation of VoiceXML document.
 * 
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
