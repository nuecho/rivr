/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.util.*;
import java.util.Map.Entry;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A {@link SubdialogueCall} is a {@link VoiceXmlOutputTurn} that invokes
 * another external dialogue.
 * <p>
 * Parameters may be passed to the subdialogue and return values may be
 * retrieved if the invoked subdialogue ends with a <code>&lt;return&gt;</code>
 * instruction.
 * 
 * @author Nu Echo Inc.
 * @see Parameter
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml2.3.4">http://www.w3.org/TR/voicexml20/#dml2.3.4</a>
 */
public class SubdialogueCall extends VoiceXmlOutputTurn {
    public static final String SUBDIALOGUE_RESULT_VARIABLE_NAME = "subdialogue";

    private static final String SUBDIALOGUE_INVOCATION_TURN_TYPE = "subdialogue";

    private static final String POST_DIALOGUE_SCRIPT_PROPERTY = "postDialogueScript";
    private static final String SUBDIALOGUE_PARAMETERS_PROPERTY = "subdialogueParameters";
    private static final String SUBMIT_URI_PROPERTY = "uri";
    private static final String SUBMIT_PARAMETERS_PROPERTY = "submitParameters";
    private static final String SUBMIT_METHOD_PROPERTY = "submitMethod";
    private static final String FETCH_CONFIGURATION_PROPERTY = "fetchConfiguration";

    private final String mUri;
    private Collection<Parameter> mParameters = Collections.emptyList();
    private VariableList mSubmitParameters = new VariableList();
    private SubmitMethod mMethod = SubmitMethod.get;
    private FetchConfiguration mFetchConfiguration;
    private String mPostDialogueScript;

    /**
     * @param name The name of this turn. Not empty.
     * @param uri The URI of the subdialogue. Not empty.
     */
    public SubdialogueCall(String name, String uri) {
        super(name);
        Assert.notEmpty(uri, "uri");
        mUri = uri;
    }

    /**
     * @param parameters A list of {@link Parameter} that will be passed to the
     *            subdialogue. Not null.
     */
    public final void setSubdialogueParameters(Collection<Parameter> parameters) {
        Assert.notNull(parameters, "subdialogueParameters");
        mParameters = new ArrayList<Parameter>(parameters);
    }

    /**
     * @param subdialogueParameters A list of {@link Parameter} that will be
     *            passed to the subdialogue. Not null.
     */
    public final void setSubdialogueParameters(Parameter... subdialogueParameters) {
        setSubdialogueParameters(asList(subdialogueParameters));
    }

    /**
     * @param submitParameters A list of variable to submit when invoking the
     *            URI. Not null.
     */
    public final void setSubmitParameters(VariableList submitParameters) {
        Assert.notNull(submitParameters, "submitParameters");
        mSubmitParameters = submitParameters;
    }

    /**
     * @param method The HTTP method used to invoke the subdialogue (GET or
     *            POST). <code>null</code> to use the VoiceXML platform default
     * @see SubmitMethod
     */
    public final void setMethod(SubmitMethod method) {
        mMethod = method;
    }

    /**
     * @param fetchConfiguration The {@link FetchConfiguration}.
     *            <code>null</code> to use the VoiceXML platform default.
     */
    public final void setSubdialogueFetchConfiguration(FetchConfiguration fetchConfiguration) {
        mFetchConfiguration = fetchConfiguration;
    }

    /**
     * @param postDialogueScript The ECMAScript script to execute after
     *            subdialogue invocation.
     */
    public final void setPostDialogueScript(String postDialogueScript) {
        mPostDialogueScript = postDialogueScript;
    }

    public final String getUri() {
        return mUri;
    }

    public final Collection<Parameter> getVoiceXmlParameters() {
        return unmodifiableCollection(mParameters);
    }

    public final VariableList getSubmitParameters() {
        return mSubmitParameters;
    }

    public final SubmitMethod getMethod() {
        return mMethod;
    }

    public final String getPostDialogueScript() {
        return mPostDialogueScript;
    }

    public final FetchConfiguration getSubdialogueFetchConfiguration() {
        return mFetchConfiguration;
    }

    @Override
    protected final String getOuputTurnType() {
        return SUBDIALOGUE_INVOCATION_TURN_TYPE;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, SUBMIT_URI_PROPERTY, mUri);
        JsonUtils.add(builder, SUBMIT_METHOD_PROPERTY, mMethod.name());
        JsonUtils.add(builder, SUBMIT_PARAMETERS_PROPERTY, mSubmitParameters);
        JsonUtils.add(builder, SUBDIALOGUE_PARAMETERS_PROPERTY, JsonUtils.toJson(mParameters));
        JsonUtils.add(builder, FETCH_CONFIGURATION_PROPERTY, mFetchConfiguration);
        JsonUtils.add(builder, POST_DIALOGUE_SCRIPT_PROPERTY, getPostDialogueScript());
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {

        List<String> submitNameList = new ArrayList<String>();
        VariableList submitVariableList = mSubmitParameters;
        if (submitVariableList != null) {
            addVariables(formElement, submitVariableList);

            for (Entry<String, String> entry : mSubmitParameters) {
                submitNameList.add(entry.getKey());
            }
        }

        Element subdialogueElement = DomUtils.appendNewElement(formElement, SUBDIALOG_ELEMENT);
        subdialogueElement.setAttribute(NAME_ATTRIBUTE, SUBDIALOGUE_FORM_ITEM_NAME);
        subdialogueElement.setAttribute(SRC_ATTRIBUTE, mUri);

        if (!submitNameList.isEmpty()) {
            subdialogueElement.setAttribute(NAME_LIST_ATTRIBUTE, StringUtils.join(submitNameList, " "));
        }

        for (Parameter parameter : mParameters) {
            Element paramElement = DomUtils.appendNewElement(subdialogueElement, PARAM_ELEMENT);
            paramElement.setAttribute(NAME_ATTRIBUTE, parameter.getName());
            setAttribute(paramElement, VALUE_ATTRIBUTE, parameter.getValue());
            setAttribute(paramElement, EXPR_ATTRIBUTE, parameter.getExpression());
        }

        SubmitMethod submitMethod = mMethod;
        if (submitMethod != null) {
            subdialogueElement.setAttribute(METHOD_ATTRIBUTE, submitMethod.name());
        }

        FetchConfiguration fetchConfiguration = mFetchConfiguration;
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
    }

    public static final class Parameter implements JsonSerializable {
        private static final String NAME_PROPERTY = "name";
        private static final String VALUE_PROPERTY = "value";
        private static final String EXPRESSION_PROPERTY = "expression";

        private final String mName;
        private String mExpression;
        private String mValue;

        /**
         * @param name The name of the parameter. Not empty.
         * @param value The string value of the parameter. Not null.
         * @return The newly created subdialogue parameter
         */
        public static Parameter createWithValue(String name, String value) {
            Assert.notNull(value, "value");

            Parameter parameter = new Parameter(name);
            parameter.mValue = value;
            return parameter;
        }

        /**
         * @param name The name of the parameter. Not empty.
         * @param json The JSON value of the parameter. Not null.
         * @return The newly created subdialogue parameter
         */
        public static Parameter createWithJson(String name, JsonValue json) {
            Assert.notNull(json, "json");
            return createWithExpression(name, json.toString());
        }

        /**
         * @param name The name of the parameter. Not empty.
         * @param expression The ECMAScript expression of the parameter. Not
         *            null.
         * @return The newly created subdialogue parameter
         */
        public static Parameter createWithExpression(String name, String expression) {
            Assert.notNull(expression, "expression");

            Parameter parameter = new Parameter(name);
            parameter.mExpression = expression;
            return parameter;
        }

        private Parameter(String name) {
            Assert.notEmpty(name, "name");
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public String getExpression() {
            return mExpression;
        }

        public String getValue() {
            return mValue;
        }

        @Override
        public JsonValue asJson() {
            JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
            JsonUtils.add(builder, NAME_PROPERTY, mName);
            JsonUtils.add(builder, EXPRESSION_PROPERTY, mExpression);
            JsonUtils.add(builder, VALUE_PROPERTY, mValue);
            return builder.build();
        }
    }

    public static final class FetchConfiguration implements JsonSerializable {

        private static final String RESOURCE_FETCH_CONFIGURATION = "resourceFetchConfiguration";
        private static final String FETCH_AUDIO = "fetchAudio";

        private ResourceFetchConfiguration mResourceFetchConfiguration;
        private String mFetchAudio;

        public ResourceFetchConfiguration getResourceFetchConfiguration() {
            return mResourceFetchConfiguration;
        }

        public String getFetchAudio() {
            return mFetchAudio;
        }

        public void setResourceFetchConfiguration(ResourceFetchConfiguration resourceFetchConfiguration) {
            mResourceFetchConfiguration = resourceFetchConfiguration;
        }

        public void setFetchAudio(String fetchAudio) {
            mFetchAudio = fetchAudio;
        }

        @Override
        public JsonValue asJson() {
            JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
            JsonUtils.add(builder, FETCH_AUDIO, mFetchAudio);
            JsonUtils.add(builder, RESOURCE_FETCH_CONFIGURATION, mResourceFetchConfiguration);
            return builder.build();
        }
    }

    public static class Builder {

        private final String mName;
        private String mUri;
        private final List<Parameter> mParameters = Collections.emptyList();
        private final VariableList mSubmitParameters = new VariableList();
        private SubmitMethod mMethod = SubmitMethod.get;
        private FetchConfiguration mFetchConfiguration;
        private String mPostDialogueScript;

        public Builder(String name) {
            mName = name;
        }

        public Builder uri(String uri) {
            Assert.notEmpty(uri, "uri");
            mUri = uri;
            return this;
        }

        public Builder addVoiceXmlParameter(Parameter parameter) {
            Assert.notNull(parameter, "parameter");
            mParameters.add(parameter);
            return this;
        }

        public Builder addSubmitParameterExpression(String name, String expression) {
            Assert.notEmpty(name, "name");
            mSubmitParameters.addWithExpression(name, expression);
            return this;
        }

        public Builder addSubmitParameterString(String name, String string) {
            Assert.notEmpty(name, "name");
            mSubmitParameters.addWithString(name, string);
            return this;
        }

        public Builder setMethod(SubmitMethod method) {
            Assert.notNull(method, "method");
            mMethod = method;
            return this;
        }

        public Builder setFetchConfiguration(FetchConfiguration fetchConfiguration) {
            mFetchConfiguration = fetchConfiguration;
            return this;
        }

        public Builder setPostDialogueScript(String postDialogueScript) {
            mPostDialogueScript = postDialogueScript;
            return this;
        }

        public SubdialogueCall build() {
            SubdialogueCall subdialogueCall = new SubdialogueCall(mName, mUri);
            subdialogueCall.setMethod(mMethod);
            subdialogueCall.setPostDialogueScript(mPostDialogueScript);
            subdialogueCall.setSubdialogueFetchConfiguration(mFetchConfiguration);
            subdialogueCall.setSubdialogueParameters(mParameters);
            subdialogueCall.setSubmitParameters(mSubmitParameters);
            return subdialogueCall;
        }
    }
}