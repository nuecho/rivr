/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.json;

import java.io.*;

import javax.json.*;
import javax.servlet.http.*;

import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.json.JsonServletResponseContent.JsonpMode;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * {@link StepRenderer} used for Rivr dialogue runner. All the
 * {@link VoiceXmlOutputTurn VoiceXmlOutputTurns} and {@link VoiceXmlLastTurn
 * VoiceXmlLastTurns} are rendered as JSON object.
 * 
 * @author Nu Echo Inc.
 */
public class JsonStepRenderer implements
        StepRenderer<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> {

    private static final String TEXTAREA_PARAMETER = "textarea";
    private static final String CALLBACK_PARAMETER = "callback";
    private static final String TYPE_OUTPUT_TURN = "outputTurn";
    private static final String TYPE_LAST_TURN = "lastTurn";
    private static final String TYPE_PROPERTY = "type";
    private static final String TURN_PROPERTY = "turn";
    private static final String SERVLET_PATH_PROPERTY = "servletPath";
    private static final String VOICE_XML_PROPERTY = "voiceXml";

    private final VoiceXmlStepRenderer mVoiceXmlStepRenderer;

    public JsonStepRenderer(VoiceXmlStepRenderer voiceXmlStepRenderer) {
        mVoiceXmlStepRenderer = voiceXmlStepRenderer;
    }

    @Override
    public ServletResponseContent createDocumentForOutputTurn(VoiceXmlOutputTurn outputTurn,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              VoiceXmlDialogueContext dialogueContext)
            throws StepRendererException {
        Assert.notNull(outputTurn, "outputTurn");
        JsonValue turn = outputTurn.asJson();

        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, TYPE_PROPERTY, TYPE_OUTPUT_TURN);
        JsonUtils.add(builder, SERVLET_PATH_PROPERTY, VoiceXmlDomUtil.getSubmitPathForTurn(dialogueContext, outputTurn));
        JsonUtils.add(builder, TURN_PROPERTY, turn);

        ServletResponseContent voiceXmlResponseContent = mVoiceXmlStepRenderer.createDocumentForOutputTurn(outputTurn,
                                                                                                           request,
                                                                                                           response,
                                                                                                           dialogueContext);

        return processResponse(request, builder, voiceXmlResponseContent);

    }

    @Override
    public ServletResponseContent createDocumentForLastTurn(VoiceXmlLastTurn lastTurn,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            VoiceXmlDialogueContext dialogueContext)
            throws StepRendererException {
        Assert.notNull(lastTurn, "lastTurn");
        JsonValue turn = lastTurn.asJson();

        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, TYPE_PROPERTY, TYPE_LAST_TURN);
        JsonUtils.add(builder, TURN_PROPERTY, turn);

        ServletResponseContent voiceXmlResponseContent = mVoiceXmlStepRenderer.createDocumentForLastTurn(lastTurn,
                                                                                                         request,
                                                                                                         response,
                                                                                                         dialogueContext);

        return processResponse(request, builder, voiceXmlResponseContent);

    }

    private JsonServletResponseContent processResponse(HttpServletRequest request,
                                                       JsonObjectBuilder builder,
                                                       ServletResponseContent voiceXmlServletResponseContent)
            throws StepRendererException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            voiceXmlServletResponseContent.writeTo(byteArrayOutputStream);
        } catch (IOException exception1) {
            throw new StepRendererException("Error while wirting VoiceXML document");
        }

        try {
            String voiceXml = new String(byteArrayOutputStream.toByteArray(), "utf-8");
            JsonUtils.add(builder, VOICE_XML_PROPERTY, voiceXml);
        } catch (UnsupportedEncodingException exception) {
            throw new AssertionError("Missing UTF-8 encoding");
        }

        String callback = request.getParameter(CALLBACK_PARAMETER);
        String textarea = request.getParameter(TEXTAREA_PARAMETER);

        JsonpMode jsonpMode = callback != null
                ? textarea != null ? JsonpMode.TEXTAREA : JsonpMode.NORMAL
                : JsonpMode.DISABLED;
        return new JsonServletResponseContent(builder.build(), jsonpMode, callback);
    }

}