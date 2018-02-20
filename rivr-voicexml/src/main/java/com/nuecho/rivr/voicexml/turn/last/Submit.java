/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.last;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Terminates a {@link VoiceXmlDialogue} with a submit, sending information to
 * the server and leaving control to the document returned by the request.
 * Submit can be performed with a <code>GET</code> or a <code>POST</code>.
 * 
 * @author Nu Echo Inc.
 * @see VoiceXmlDialogue
 * @see LastTurn
 * @see SubmitMethod
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml5.3.8">http://www.w3.org/TR/voicexml20/#dml5.3.8</a>
 */
public class Submit extends VoiceXmlLastTurn {

    private static final String METHOD_PROPERTY = "method";
    private static final String MEDIA_ENCODING_TYPE_PROPERTY = "mediaEncodingType";
    private static final String FETCH_CONFIGURATION_PROPERTY = "fetchConfiguration";
    private static final String URI_PROPERTY = "uri";
    private static final String VARIABLES_PROPERTY = "variables";

    private final VariableList mVariables;
    private final String mUri;

    private SubmitMethod mMethod = SubmitMethod.get;
    private String mMediaEncodingType;

    private DocumentFetchConfiguration mFetchConfiguration;

    public Submit(String name, String uri, VariableList variables) {
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

    public VariableList getVariables() {
        return mVariables;
    }

    public String getUri() {
        return mUri;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, VARIABLES_PROPERTY, mVariables);
        JsonUtils.add(builder, URI_PROPERTY, mUri);
        JsonUtils.add(builder, FETCH_CONFIGURATION_PROPERTY, mFetchConfiguration);
        JsonUtils.add(builder, MEDIA_ENCODING_TYPE_PROPERTY, mMediaEncodingType);
        if (mMethod != null) {
            JsonUtils.add(builder, METHOD_PROPERTY, mMethod.name());
        }
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
        Element submitElement = document.createElement(SUBMIT_ELEMENT);
        VoiceXmlDomUtil.setAttribute(submitElement, VoiceXmlDomUtil.NEXT_ATTRIBUTE, mUri);
        addNamelist(blockElement, submitElement, mVariables);
        VoiceXmlDomUtil.applyDocumentFetchConfiguration(submitElement, mFetchConfiguration);
        blockElement.appendChild(submitElement);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((mFetchConfiguration == null) ? 0 : mFetchConfiguration.hashCode());
        result = prime * result + ((mMediaEncodingType == null) ? 0 : mMediaEncodingType.hashCode());
        result = prime * result + ((mMethod == null) ? 0 : mMethod.hashCode());
        result = prime * result + ((mUri == null) ? 0 : mUri.hashCode());
        result = prime * result + ((mVariables == null) ? 0 : mVariables.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Submit other = (Submit) obj;
        if (mFetchConfiguration == null) {
            if (other.mFetchConfiguration != null) return false;
        } else if (!mFetchConfiguration.equals(other.mFetchConfiguration)) return false;
        if (mMediaEncodingType == null) {
            if (other.mMediaEncodingType != null) return false;
        } else if (!mMediaEncodingType.equals(other.mMediaEncodingType)) return false;
        if (mMethod != other.mMethod) return false;
        if (mUri == null) {
            if (other.mUri != null) return false;
        } else if (!mUri.equals(other.mUri)) return false;
        if (mVariables == null) {
            if (other.mVariables != null) return false;
        } else if (!mVariables.equals(other.mVariables)) return false;
        return true;
    }

}