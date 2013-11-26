/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.voicexml;

import java.util.*;

import javax.servlet.http.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * VoiceXML specialization of {@link StepRenderer}. {@link VoiceXmlOutputTurn
 * VoiceXmlOutputTurns} and {@link VoiceXmlLastTurn VoiceXmlLastTurns} are
 * converted to VoiceXML documents.
 * 
 * @author Nu Echo Inc.
 */
public class VoiceXmlStepRenderer implements
        StepRenderer<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> {

    private static final String VOICE_XML_MIME_TYPE = "application/voicexml+xml";

    private final List<VoiceXmlDocumentAdapter> mVoiceXmlDocumentAdapters;

    public VoiceXmlStepRenderer(List<? extends VoiceXmlDocumentAdapter> voiceXmlDocumentAdapters) {
        if (voiceXmlDocumentAdapters != null) {
            mVoiceXmlDocumentAdapters = new ArrayList<VoiceXmlDocumentAdapter>(voiceXmlDocumentAdapters);
        } else {
            mVoiceXmlDocumentAdapters = null;
        }
    }

    @Override
    public ServletResponseContent createDocumentForOutputTurn(VoiceXmlOutputTurn outputTurn,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              VoiceXmlDialogueContext voiceXmlDialogueContext)
            throws StepRendererException {
        return renderTurn(outputTurn, voiceXmlDialogueContext);
    }

    @Override
    public ServletResponseContent createDocumentForLastTurn(VoiceXmlLastTurn lastTurn,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            VoiceXmlDialogueContext voiceXmlDialogueContext)
            throws StepRendererException {
        return renderTurn(lastTurn, voiceXmlDialogueContext);
    }

    private ServletResponseContent renderTurn(VoiceXmlDocumentTurn turn, VoiceXmlDialogueContext voiceXmlDialogueContext)
            throws StepRendererException {
        Assert.notNull(turn, "turn");
        Document voiceXmlDocument;
        try {
            voiceXmlDocument = turn.getVoiceXmlDocument(voiceXmlDialogueContext);
        } catch (VoiceXmlDocumentRenderingException exception) {
            throw new StepRendererException(exception);
        }

        try {
            if (mVoiceXmlDocumentAdapters != null) {
                for (VoiceXmlDocumentAdapter adapter : mVoiceXmlDocumentAdapters) {
                    adapter.adaptVoiceXmlDocument(voiceXmlDocument);
                }
            }
        } catch (VoiceXmlDocumentRenderingException exception) {
            throw new StepRendererException("Error white applying adapter.", exception);
        }

        voiceXmlDialogueContext.incrementTurnIndex();
        return new XmlDocumentServletResponseContent(voiceXmlDocument, VOICE_XML_MIME_TYPE);
    }

}
