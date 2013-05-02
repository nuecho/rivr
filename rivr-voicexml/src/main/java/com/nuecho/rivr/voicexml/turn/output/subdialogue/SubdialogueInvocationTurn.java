/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.subdialogue;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;
import static java.util.Arrays.*;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class SubdialogueInvocationTurn extends VoiceXmlOutputTurn {

    private static final String SUBDIALOGUE_RESULT_VARIABLE_NAME = "subdialogue";

    private static final String SUBDIALOGUE_TYPE = "subdialogue";
    private static final String POST_DIALOGUE_SCRIPT_PROPERTY = "postDialogueScript";
    private static final String VOICE_XML_PARAMETERS_PROPERTY = "voiceXmlParameters";
    private static final String SUBMIT_URI_PROPERTY = "uri";
    private static final String SUBMIT_PARAMETERS_PROPERTY = "submitParameters";
    private static final String SUBMIT_METHOD_PROPERTY = "submitMethod";

    private final String mUri;
    private List<SubdialogueParameter> mVoiceXmlParameters = Collections.emptyList();
    private VariableDeclarationList mSubmitParameters = new VariableDeclarationList();
    private SubdialogueSubmitMethod mMethod = SubdialogueSubmitMethod.GET;
    private SubdialogueFetchConfiguration mSubdialogueFetchConfiguration;
    private String mPostDialogueScript;

    public SubdialogueInvocationTurn(String name, String uri) {
        super(name);
        mUri = uri;
    }

    public void setVoiceXmlParameters(List<SubdialogueParameter> voiceXmlParameters) {
        Assert.notNull(voiceXmlParameters, "voiceXmlParameters");
        mVoiceXmlParameters = new ArrayList<SubdialogueParameter>(voiceXmlParameters);
    }

    public void setVoiceXmlParameters(SubdialogueParameter... voiceXmlParameters) {
        setVoiceXmlParameters(asList(voiceXmlParameters));
    }

    public void setSubmitParameters(VariableDeclarationList submitParameters) {
        Assert.notNull(submitParameters, "submitParameters");
        mSubmitParameters = submitParameters;
    }

    public void setMethod(SubdialogueSubmitMethod method) {
        mMethod = method;
    }

    public String getUri() {
        return mUri;
    }

    public List<? extends SubdialogueParameter> getVoiceXmlParameters() {
        return Collections.unmodifiableList(mVoiceXmlParameters);
    }

    public VariableDeclarationList getSubmitParameters() {
        return mSubmitParameters;
    }

    public SubdialogueSubmitMethod getMethod() {
        return mMethod;
    }

    public String getPostDialogueScript() {
        return mPostDialogueScript;
    }

    public void setPostDialogueScript(String postDialogueScript) {
        mPostDialogueScript = postDialogueScript;
    }

    public SubdialogueFetchConfiguration getSubdialogueFetchConfiguration() {
        return mSubdialogueFetchConfiguration;
    }

    public void setSubdialogueFetchConfiguration(SubdialogueFetchConfiguration subdialogueFetchConfiguration) {
        mSubdialogueFetchConfiguration = subdialogueFetchConfiguration;
    }

    @Override
    protected String getOuputTurnType() {
        return SUBDIALOGUE_TYPE;
    }

    @Override
    protected JsonValue getTurnAsJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, SUBMIT_URI_PROPERTY, mUri);
        JsonUtils.add(builder, SUBMIT_METHOD_PROPERTY, mMethod.getKey());
        JsonUtils.add(builder, POST_DIALOGUE_SCRIPT_PROPERTY, getPostDialogueScript());
        JsonUtils.add(builder, VOICE_XML_PARAMETERS_PROPERTY, JsonUtils.toJson(mVoiceXmlParameters));
        JsonUtils.add(builder, SUBMIT_PARAMETERS_PROPERTY, mSubmitParameters);
        JsonUtils.add(builder, SUBMIT_PARAMETERS_PROPERTY, mSubdialogueFetchConfiguration);
        return builder.build();
    }

    @Override
    public Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext, null);
        Element formElement = createForm(document);

        List<String> submitNameList = new ArrayList<String>();
        VariableDeclarationList submitVariableList = mSubmitParameters;
        if (submitVariableList != null) {
            addVariableDeclarations(formElement, submitVariableList);

            for (VariableDeclaration declaration : mSubmitParameters) {
                submitNameList.add(declaration.getName());
            }
        }

        Element subdialogueElement = DomUtils.appendNewElement(formElement, SUBDIALOG_ELEMENT);
        subdialogueElement.setAttribute(NAME_ATTRIBUTE, SUBDIALOGUE_FORM_ITEM_NAME);
        subdialogueElement.setAttribute(SRC_ATTRIBUTE, mUri);

        if (!submitNameList.isEmpty()) {
            subdialogueElement.setAttribute(NAME_LIST_ATTRIBUTE, StringUtils.join(submitNameList, " "));
        }

        for (SubdialogueParameter subdialogueParameter : Collections.unmodifiableList(mVoiceXmlParameters)) {
            Element paramElement = DomUtils.appendNewElement(subdialogueElement, PARAM_ELEMENT);
            paramElement.setAttribute(NAME_ATTRIBUTE, subdialogueParameter.getName());
            paramElement.setAttribute(EXPR_ATTRIBUTE, subdialogueParameter.getExpression());
        }

        SubdialogueSubmitMethod submitMethod = mMethod;
        if (submitMethod != null) {
            subdialogueElement.setAttribute(METHOD_ATTRIBUTE, submitMethod.getKey());
        }

        SubdialogueFetchConfiguration fetchConfiguration = mSubdialogueFetchConfiguration;
        if (fetchConfiguration != null) {
            applyFetchAudio(subdialogueElement, fetchConfiguration.getFetchAudio());
            applyRessourceFetchConfiguration(subdialogueElement, fetchConfiguration.getResourceFetchConfiguration());
        }

        Element filledElement = DomUtils.appendNewElement(subdialogueElement, FILLED_ELEMENT);

        createVarElement(filledElement, SUBDIALOGUE_RESULT_VARIABLE_NAME, "dialog." + SUBDIALOGUE_FORM_ITEM_NAME);

        if (mPostDialogueScript != null) {
            createScript(filledElement, mPostDialogueScript);
        }

        createScript(filledElement, RIVR_SCOPE_OBJECT + ".addValueResult(" + SUBDIALOGUE_RESULT_VARIABLE_NAME + ");");
        createGotoSubmit(filledElement);

        addSubmitForm(dialogueContext, document, this);

        return document;
    }

}