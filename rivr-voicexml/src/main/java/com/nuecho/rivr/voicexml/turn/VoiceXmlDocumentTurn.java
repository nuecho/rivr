/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class VoiceXmlDocumentTurn implements JsonSerializable, NamedTurn {

    private static final String DATA_PROPERTY = "data";
    private static final String NAME_PROPERTY = "name";

    private final String mName;

    private List<VoiceXmlDocumentAdapter> mAdapters;

    public VoiceXmlDocumentTurn(String name) {
        Assert.notEmpty(name, "name");
        mName = name;
    }

    protected abstract JsonValue getTurnAsJson();

    protected abstract Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException;

    @Override
    public final String getName() {
        return mName;
    }

    public final Document getVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createVoiceXmlDocument(dialogueContext);
        if (mAdapters != null) {
            for (VoiceXmlDocumentAdapter adapter : mAdapters) {
                adapter.adaptVoiceXmlDocument(document);
            }
        }
        return document;
    }

    @Override
    public final String toString() {
        return asJson().toString();
    }

    @Override
    public final JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, NAME_PROPERTY, getName());
        JsonUtils.add(builder, DATA_PROPERTY, getTurnAsJson());
        putAdditionalTopLevelData(builder);
        return builder.build();
    }

    /**
     * @param builder
     */
    protected void putAdditionalTopLevelData(JsonObjectBuilder builder) {}

}
