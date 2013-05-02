/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.servlet.http.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.servlet.session.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.first.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * @author Nu Echo Inc.
 */
public class DefaultVoiceXmlRootDocumentFactory implements VoiceXmlRootDocumentFactory {

    @Override
    public Document getDocument(HttpServletRequest request,
                                Session<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> session) {

        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();

        VoiceXmlDialogueContext dialogueContext = session.getDialogueContext();
        Element vxmlElement = createVoiceXmlDocumentRoot(dialogueContext);
        createVarElement(vxmlElement, RIVR_VARIABLE, "{}");
        addScript(vxmlElement, VoiceXmlDialogueServlet.RIVR_SCRIPT, contextPath + servletPath);
        addNonFatalEventHandlers(vxmlElement, dialogueContext);
        return vxmlElement.getOwnerDocument();
    }

    private static void addScript(Element parent, String scriptPath, String contextPath) {
        Element scriptElement = DomUtils.appendNewElement(parent, SCRIPT_ELEMENT);
        scriptElement.setAttribute(SRC_ATTRIBUTE, contextPath + scriptPath);
    }
}
