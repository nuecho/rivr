/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import javax.servlet.http.*;

import org.w3c.dom.*;

import com.nuecho.rivr.voicexml.rendering.voicexml.*;

/**
 * @author Nu Echo Inc.
 */
public interface VoiceXmlRootDocumentFactory {

    Document getDocument(HttpServletRequest request) throws VoiceXmlDocumentRenderingException;

}
