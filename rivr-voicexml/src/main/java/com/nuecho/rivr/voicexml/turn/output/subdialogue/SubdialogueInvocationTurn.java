/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.subdialogue;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>SubdialogueInvocationTurn</code> is a {@link VoiceXmlOutputTurn} that
 * invokes another external dialogue.
 * <p>
 * Parameters may be passed to the subdialogue and return values may be
 * retrieved if the subdialogue ends with a {@link VoiceXmlReturnTurn}.
 * 
 * @author Nu Echo Inc.
 * @see SubdialogueParameter
 * @see SubdialogueSubmitMethod
 * @see <a href="http://www.w3.org/TR/voicexml20/#dml2.3.4">http://www.w3.org/TR/voicexml20/#dml2.3.4</a>
 */
public class SubdialogueInvocationTurn extends VoiceXmlOutputTurn {
    public static final String SUBDIALOGUE_RESULT_VARIABLE_NAME = "subdialogue";

    private static final String SUBDIALOGUE_INVOCATION_TURN_TYPE = "subdialogue";

    private static final String POST_DIALOGUE_SCRIPT_PROPERTY = "postDialogueScript";
    private static final String SUBDIALOGUE_PARAMETERS_PROPERTY = "subdialogueParameters";
    private static final String SUBMIT_URI_PROPERTY = "uri";
    private static final String SUBMIT_PARAMETERS_PROPERTY = "submitParameters";
    private static final String SUBMIT_METHOD_PROPERTY = "submitMethod";
    private static final String SUBDIALOGUE_FETCH_CONFIGURATION_PROPERTY = "subdialogueFetchConfiguration";

    private final String mUri;
    private List<SubdialogueParameter> mSubdialogueParameters = Collections.emptyList();
    private VariableDeclarationList mSubmitParameters = new VariableDeclarationList();
    private SubdialogueSubmitMethod mMethod = SubdialogueSubmitMethod.GET;
    private SubdialogueFetchConfiguration mSubdialogueFetchConfiguration;
    private String mPostDialogueScript;

    /**
     * @param name The name of this turn. Not empty.
     * @param uri The URI of the subdialogue. Not empty.
     */
    public SubdialogueInvocationTurn(String name, String uri) {
        super(name);
        Assert.notEmpty(uri, "uri");
        mUri = uri;
    }

    /**
     * @param subdialogueParameters A list of {@link SubdialogueParameter} that
     *            will be passed to the subdialogue. Not null.
     */
    public final void setSubdialogueParameters(List<SubdialogueParameter> subdialogueParameters) {
        Assert.notNull(subdialogueParameters, "subdialogueParameters");
        mSubdialogueParameters = new ArrayList<SubdialogueParameter>(subdialogueParameters);
    }

    /**
     * @param subdialogueParameters A list of {@link SubdialogueParameter} that
     *            will be passed to the subdialogue. Not null.
     */
    public final void setSubdialogueParameters(SubdialogueParameter... subdialogueParameters) {
        setSubdialogueParameters(asList(subdialogueParameters));
    }

    /**
     * @param subdialogueParameters A list of variable to submit when invoking
     *            the URI. Not null.
     */
    public final void setSubmitParameters(VariableDeclarationList submitParameters) {
        Assert.notNull(submitParameters, "submitParameters");
        mSubmitParameters = submitParameters;
    }

    /**
     * @param method The HTTP method used to invoke the subdialogue (GET or
     *            POST). Null reverts to VoiceXML default value.
     * @see SubdialogueSubmitMethod
     */
    public final void setMethod(SubdialogueSubmitMethod method) {
        mMethod = method;
    }

    /**
     * @param subdialogueFetchConfiguration The
     *            {@link SubdialogueFetchConfiguration}. Null reverts to
     *            VoiceXML default value.
     */
    public final void setSubdialogueFetchConfiguration(SubdialogueFetchConfiguration subdialogueFetchConfiguration) {
        mSubdialogueFetchConfiguration = subdialogueFetchConfiguration;
    }

    /**
     * @param postObjectScript The ECMAScript script to execute after
     *            subdialogue invocation.
     */
    public final void setPostDialogueScript(String postDialogueScript) {
        mPostDialogueScript = postDialogueScript;
    }

    public final String getUri() {
        return mUri;
    }

    public final List<? extends SubdialogueParameter> getVoiceXmlParameters() {
        return unmodifiableList(mSubdialogueParameters);
    }

    public final VariableDeclarationList getSubmitParameters() {
        return mSubmitParameters;
    }

    public final SubdialogueSubmitMethod getMethod() {
        return mMethod;
    }

    public final String getPostDialogueScript() {
        return mPostDialogueScript;
    }

    public final SubdialogueFetchConfiguration getSubdialogueFetchConfiguration() {
        return mSubdialogueFetchConfiguration;
    }

    @Override
    protected final String getOuputTurnType() {
        return SUBDIALOGUE_INVOCATION_TURN_TYPE;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, SUBMIT_URI_PROPERTY, mUri);
        JsonUtils.add(builder, SUBMIT_METHOD_PROPERTY, mMethod.getKey());
        JsonUtils.add(builder, SUBMIT_PARAMETERS_PROPERTY, mSubmitParameters);
        JsonUtils.add(builder, SUBDIALOGUE_PARAMETERS_PROPERTY, JsonUtils.toJson(mSubdialogueParameters));
        JsonUtils.add(builder, SUBDIALOGUE_FETCH_CONFIGURATION_PROPERTY, mSubdialogueFetchConfiguration);
        JsonUtils.add(builder, POST_DIALOGUE_SCRIPT_PROPERTY, getPostDialogueScript());
    }

    @Override
    protected Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
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

        for (SubdialogueParameter subdialogueParameter : mSubdialogueParameters) {
            Element paramElement = DomUtils.appendNewElement(subdialogueElement, PARAM_ELEMENT);
            paramElement.setAttribute(NAME_ATTRIBUTE, subdialogueParameter.getName());
            setAttribute(paramElement, VALUE_ATTRIBUTE, subdialogueParameter.getValue());
            setAttribute(paramElement, EXPR_ATTRIBUTE, subdialogueParameter.getExpression());
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
        addFatalErrorHandlerForm(dialogueContext, document, this);

        return document;
    }
}