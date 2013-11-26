/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.servlet.http.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;

/**
 * Generates the standard Rivr root document. The root document will:
 * <ul>
 * <li>declare the <code>rivr</code> variable (an object)</li>
 * <li>load the <code>rivr.js</code> script which contains the necessary
 * JavaScript methods for result manipulation</li>
 * </ul>
 * 
 * @author Nu Echo Inc.
 */
public class DefaultVoiceXmlRootDocumentFactory implements VoiceXmlRootDocumentFactory {

    @Override
    public Document getDocument(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();

        Element vxmlElement = createVoiceXmlDocumentRoot();
        createVarElement(vxmlElement, RIVR_VARIABLE, "new Object()");

        Element scriptElement = DomUtils.appendNewElement(vxmlElement, SCRIPT_ELEMENT);
        scriptElement.setAttribute(SRC_ATTRIBUTE, contextPath + servletPath + VoiceXmlDialogueServlet.RIVR_SCRIPT);
        return vxmlElement.getOwnerDocument();
    }

}
