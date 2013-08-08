/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.object;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * An <code>ObjectTurn</code> is a {@link VoiceXmlOutputTurn} used to exploit
 * platform-specific functionality.
 * 
 * @author Nu Echo Inc.
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml2.3.5">http://www.w3.org/TR/voicexml20/#dml2.3.5</a>
 */
public class ObjectTurn extends VoiceXmlOutputTurn {
    public static final String OBJECT_RESULT_VARIABLE_NAME = "object";

    private static final String OBJECT_TURN_TYPE = "object";

    private static final String ARCHIVES_PROPERTY = "archives";
    private static final String PARAMETERS_PROPERTY = "parameters";
    private static final String POST_OBJECT_SCRIPT_PROPERTY = "postObjectScript";
    private static final String TYPE_PROPERTY = "type";
    private static final String DATA_PROPERTY = "data";
    private static final String CODE_TYPE_PROPERTY = "codeType";
    private static final String CODE_BASE_PROPERTY = "codeBase";
    private static final String CLASS_ID_PROPERTY = "classId";
    private static final String FETCH_CONFIGURATION_PROPERTY = "fetchConfiguration";

    private String mClassId;
    private String mCodeBase;
    private String mCodeType;
    private String mData;
    private String mType;
    private List<String> mArchives;
    private ResourceFetchConfiguration mFetchConfiguration;
    private List<ObjectParameter> mParameters = Collections.emptyList();
    private String mPostObjectScript;

    /**
     * @param name The name of this turn. Not empty.
     */
    public ObjectTurn(String name) {
        super(name);
    }

    /**
     * @param classId The URI specifying the location of the object's
     *            implementation.
     */
    public final void setClassId(String classId) {
        mClassId = classId;
    }

    /**
     * @param codeBase The base path used to resolve relative URIs specified by
     *            classid, data, and archive.
     */
    public final void setCodeBase(String codeBase) {
        mCodeBase = codeBase;
    }

    /**
     * @param codeType The content type of data expected when downloading the
     *            object specified by classid.
     */
    public final void setCodeType(String codeType) {
        mCodeType = codeType;
    }

    /**
     * @param data The URI specifying the location of the object's data.
     */
    public final void setData(String data) {
        mData = data;
    }

    /**
     * @param type The content type of the data specified by the data attribute
     */
    public final void setType(String type) {
        mType = type;
    }

    /**
     * @param archives A list of URIs for archives containing resources relevant
     *            to the object.
     */
    public final void setArchives(List<String> archives) {
        mArchives = archives;
    }

    /**
     * @param fetchConfiguration The object {@link ResourceFetchConfiguration}.
     */
    public final void setFetchConfiguration(ResourceFetchConfiguration fetchConfiguration) {
        mFetchConfiguration = fetchConfiguration;
    }

    /**
     * @param parameters A list of parameters passed when invoking object. Not
     *            null.
     */
    public final void setParameters(List<ObjectParameter> parameters) {
        Assert.notNull(parameters, "parameters");
        mParameters = new ArrayList<ObjectParameter>(parameters);
    }

    /**
     * @param parameters A list of parameters passed when invoking object. Not
     *            null.
     */
    public final void setParameters(ObjectParameter... parameters) {
        setParameters(asList(parameters));
    }

    /**
     * @param postObjectScript The ECMAScript script to execute after object
     *            invocation.
     */
    public final void setPostObjectScript(String postObjectScript) {
        mPostObjectScript = postObjectScript;
    }

    public final String getClassId() {
        return mClassId;
    }

    public final String getCodeBase() {
        return mCodeBase;
    }

    public final String getCodeType() {
        return mCodeType;
    }

    public final String getData() {
        return mData;
    }

    public final String getType() {
        return mType;
    }

    public final List<String> getArchives() {
        return mArchives;
    }

    public final ResourceFetchConfiguration getFetchConfiguration() {
        return mFetchConfiguration;
    }

    public final List<ObjectParameter> getParameters() {
        return unmodifiableList(mParameters);
    }

    public final String getPostObjectScript() {
        return mPostObjectScript;
    }

    @Override
    protected final String getOuputTurnType() {
        return OBJECT_TURN_TYPE;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, CLASS_ID_PROPERTY, mClassId);
        JsonUtils.add(builder, CODE_BASE_PROPERTY, mCodeBase);
        JsonUtils.add(builder, CODE_TYPE_PROPERTY, mCodeType);
        JsonUtils.add(builder, DATA_PROPERTY, mData);
        JsonUtils.add(builder, TYPE_PROPERTY, mType);
        JsonUtils.add(builder, POST_OBJECT_SCRIPT_PROPERTY, mPostObjectScript);

        JsonUtils.add(builder, PARAMETERS_PROPERTY, JsonUtils.toJson(mParameters));
        JsonUtils.add(builder, FETCH_CONFIGURATION_PROPERTY, mFetchConfiguration);

        if (mArchives != null) {
            JsonArrayBuilder archiveBuilder = JsonUtils.createArrayBuilder();
            for (String archive : mArchives) {
                archiveBuilder.add(archive);
            }
            JsonUtils.add(builder, ARCHIVES_PROPERTY, archiveBuilder);
        }
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {

        Element objectElement = DomUtils.appendNewElement(formElement, OBJECT_ELEMENT);
        objectElement.setAttribute(NAME_ATTRIBUTE, OBJECT_FORM_ITEM_NAME);

        List<String> archives = getArchives();
        if (archives != null && !archives.isEmpty()) {
            objectElement.setAttribute(ARCHIVE_ATTRIBUTE, StringUtils.join(archives, " "));
        }

        setAttribute(objectElement, CLASS_ID_ATTRIBUTE, mClassId);
        setAttribute(objectElement, CODE_BASE_ATTRIBUTE, mCodeBase);
        setAttribute(objectElement, CODE_TYPE_ATTRIBUTE, mCodeType);
        setAttribute(objectElement, DATA_ATTRIBUTE, mData);
        setAttribute(objectElement, TYPE_ATTRIBUTE, mType);

        for (ObjectParameter parameter : mParameters) {
            Element paramElement = DomUtils.appendNewElement(objectElement, PARAM_ELEMENT);
            paramElement.setAttribute(NAME_ATTRIBUTE, parameter.getName());

            setAttribute(paramElement, VALUE_ATTRIBUTE, parameter.getValue());
            setAttribute(paramElement, EXPR_ATTRIBUTE, parameter.getExpression());
            setAttribute(paramElement, TYPE_ATTRIBUTE, parameter.getType());
            ParameterValueType valueType = parameter.getValueType();
            if (valueType != null) {
                paramElement.setAttribute(VALUE_TYPE_ATTRIBUTE, valueType.getKey());
            }
        }

        ResourceFetchConfiguration fetchConfiguration = mFetchConfiguration;
        if (fetchConfiguration != null) {
            applyRessourceFetchConfiguration(objectElement, fetchConfiguration);
        }

        Element filledElement = DomUtils.appendNewElement(objectElement, FILLED_ELEMENT);

        createVarElement(filledElement, OBJECT_RESULT_VARIABLE_NAME, "dialog." + OBJECT_FORM_ITEM_NAME);

        if (getPostObjectScript() != null) {
            createScript(filledElement, mPostObjectScript);
        }

        createScript(filledElement, RIVR_SCOPE_OBJECT + ".addValueResult(" + OBJECT_RESULT_VARIABLE_NAME + ");");
        createGotoSubmit(filledElement);
    }
}
