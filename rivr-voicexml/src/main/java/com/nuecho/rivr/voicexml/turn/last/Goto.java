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
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Terminates a {@link VoiceXmlDialogue} with a goto, transferring the control
 * to another application specified with a URI.
 * 
 * @author Nu Echo Inc.
 * @see VoiceXmlDialogue
 * @see LastTurn
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml5.3.7">http://www.w3.org/TR/voicexml20/#dml5.3.7</a>
 */
public class Goto extends VoiceXmlLastTurn {

    private static final String FETCH_CONFIGURATION_PROPERTY = "fetchConfiguration";
    private static final String URI_PROPERTY = "uri";

    private final String mUri;
    private DocumentFetchConfiguration mFetchConfiguration;

    public Goto(String name, String uri) {
        super(name);
        Assert.notNull(uri, "uri");
        mUri = uri;
    }

    public DocumentFetchConfiguration getFetchConfiguration() {
        return mFetchConfiguration;
    }

    public void setFetchConfiguration(DocumentFetchConfiguration fetchConfiguration) {
        mFetchConfiguration = fetchConfiguration;
    }

    public String getUri() {
        return mUri;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, URI_PROPERTY, mUri);
        JsonUtils.add(builder, FETCH_CONFIGURATION_PROPERTY, mFetchConfiguration);
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Element blockElement = DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
        Element submitElement = DomUtils.appendNewElement(blockElement, SUBMIT_ELEMENT);

        VoiceXmlDomUtil.setAttribute(submitElement, VoiceXmlDomUtil.NEXT_ATTRIBUTE, mUri);

        if (mFetchConfiguration != null) {
            VoiceXmlDomUtil.applyDocumentFetchConfiguration(submitElement, dialogueContext);
        }
    }
}
