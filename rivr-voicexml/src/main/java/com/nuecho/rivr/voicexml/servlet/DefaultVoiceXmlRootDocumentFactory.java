/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.servlet.http.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public class DefaultVoiceXmlRootDocumentFactory implements VoiceXmlRootDocumentFactory {

    @Override
    public Document getDocument(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        return createElement(contextPath, servletPath);
    }

    protected Document createElement(String contextPath, String servletPath) {
        Element vxmlElement = createVoiceXmlDocumentRoot();
        createVarElement(vxmlElement, RIVR_VARIABLE, null);

        addScript(vxmlElement, VoiceXmlDialogueServlet.RIVR_SCRIPT, contextPath + servletPath);
        return vxmlElement.getOwnerDocument();
    }

    private static void addScript(Element parent, String scriptPath, String contextPath) {
        Element scriptElement = DomUtils.appendNewElement(parent, SCRIPT_ELEMENT);
        scriptElement.setAttribute(SRC_ATTRIBUTE, contextPath + scriptPath);
    }

}
