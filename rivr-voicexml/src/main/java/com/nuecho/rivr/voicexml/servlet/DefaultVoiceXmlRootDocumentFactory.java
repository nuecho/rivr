/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import java.io.*;

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

        VoiceXmlDialogueContext dialogueContext = session.getDialogueContext();
        String contextPath = dialogueContext.getContextPath();
        String servletPath = dialogueContext.getServletPath();
        String sessionId = session.getId();
        return createElement(contextPath, servletPath, sessionId, dialogueContext);
    }

    protected Document createElement(String contextPath,
                                     String servletPath,
                                     String sessionId,
                                     VoiceXmlDialogueContext dialogueContext) {
        Element vxmlElement = createVoiceXmlDocumentRoot(dialogueContext);
        createVarElement(vxmlElement, RIVR_VARIABLE, "{\"" + RIVR_DIALOGUE_ID_PROPERTY + "\": \"" + sessionId + "\"}");

        addScript(vxmlElement, VoiceXmlDialogueServlet.RIVR_SCRIPT, contextPath + servletPath);
        addEventHandlers(vxmlElement);
        return vxmlElement.getOwnerDocument();
    }

    private static void addScript(Element parent, String scriptPath, String contextPath) {
        Element scriptElement = DomUtils.appendNewElement(parent, SCRIPT_ELEMENT);
        scriptElement.setAttribute(SRC_ATTRIBUTE, contextPath + scriptPath);
    }

    public static void main(String[] args) throws IOException {
        VoiceXmlDialogueContext context = new VoiceXmlDialogueContext(null, null, "", "", "");
        System.out.println(DomUtils.writeToString(new DefaultVoiceXmlRootDocumentFactory().createElement("",
                                                                                                         "",
                                                                                                         "sessionId",
                                                                                                         context)));
    }
}
