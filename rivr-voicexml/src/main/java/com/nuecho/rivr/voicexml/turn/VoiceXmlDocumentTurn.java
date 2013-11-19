/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class VoiceXmlDocumentTurn implements JsonSerializable {

    private static final String DATA_PROPERTY = "data";
    private static final String NAME_PROPERTY = "name";

    private final String mName;

    private final List<VoiceXmlDocumentAdapter> mAdapters = new ArrayList<VoiceXmlDocumentAdapter>();

    public VoiceXmlDocumentTurn(String name) {
        Assert.notEmpty(name, "name");
        mName = name;
    }

    protected abstract Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException;

    /**
     * @param builder
     */
    protected void addTopLevelProperties(JsonObjectBuilder builder) {}

    protected abstract void addTurnProperties(JsonObjectBuilder builder);

    public final void addAdapter(VoiceXmlDocumentAdapter adapter) {
        mAdapters.add(adapter);
    }

    public final String getName() {
        return mName;
    }

    public final Document getVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createVoiceXmlDocument(dialogueContext);
        for (VoiceXmlDocumentAdapter adapter : mAdapters) {
            adapter.adaptVoiceXmlDocument(document);
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
        JsonObjectBuilder dataBuilder = JsonUtils.createObjectBuilder();
        addTurnProperties(dataBuilder);

        JsonUtils.add(builder, NAME_PROPERTY, getName());
        JsonUtils.add(builder, DATA_PROPERTY, dataBuilder.build());
        addTopLevelProperties(builder);
        return builder.build();
    }
}