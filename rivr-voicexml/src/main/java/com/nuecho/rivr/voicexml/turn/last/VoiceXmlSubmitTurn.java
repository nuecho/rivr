/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.last;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class VoiceXmlSubmitTurn extends VoiceXmlLastTurn {

    private static final String METHOD_PROPERTY = "method";
    private static final String MEDIA_ENCODING_TYPE_PROPERTY = "mediaEncodingType";
    private static final String FETCH_CONFIGURATION_PROPERTY = "fetchConfiguration";
    private static final String URI_PROPERTY = "uri";
    private static final String VARIABLES_PROPERTY = "variables";

    private final VariableDeclarationList mVariables;
    private final String mUri;

    private SubmitMethod mMethod = SubmitMethod.GET;
    private String mMediaEncodingType;

    private DocumentFetchConfiguration mFetchConfiguration;

    public VoiceXmlSubmitTurn(String name, String uri, VariableDeclarationList variables) {
        super(name);
        Assert.notNull(uri, "uri");
        mVariables = variables;
        mUri = uri;
    }

    public DocumentFetchConfiguration getFetchConfiguration() {
        return mFetchConfiguration;
    }

    public void setFetchConfiguration(DocumentFetchConfiguration fetchConfiguration) {
        mFetchConfiguration = fetchConfiguration;
    }

    public void setMethod(SubmitMethod method) {
        mMethod = method;
    }

    public SubmitMethod getMethod() {
        return mMethod;
    }

    public String getMediaEncodingType() {
        return mMediaEncodingType;
    }

    public void setMediaEncodingType(String mediaEncodingType) {
        mMediaEncodingType = mediaEncodingType;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
        JsonUtils.add(builder, URI_PROPERTY, mUri);
        JsonUtils.add(builder, FETCH_CONFIGURATION_PROPERTY, mFetchConfiguration);
        JsonUtils.add(builder, MEDIA_ENCODING_TYPE_PROPERTY, mMediaEncodingType);
        if (mMethod != null) {
            JsonUtils.add(builder, METHOD_PROPERTY, mMethod.getValue());
        }
    }

    @Override
    protected Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext, this);
        Element formElement = createForm(document);
        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
        Element submitElement = document.createElement(SUBMIT_ELEMENT);
        VoiceXmlDomUtil.setAttribute(submitElement, VoiceXmlDomUtil.NEXT_ATTRIBUTE, mUri);
        addNamelist(blockElement, submitElement, mVariables);
        VoiceXmlDomUtil.applyDocumentFetchConfiguration(submitElement, mFetchConfiguration);
        blockElement.appendChild(submitElement);
        addFatalErrorHandlerForm(dialogueContext, document, this);

        return document;
    }
}
