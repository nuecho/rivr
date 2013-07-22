/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public class MessageTurn extends VoiceXmlOutputTurn {

    private static final String BARGE_IN_PROPERTY = "bargeIn";
    private static final String LANGUAGE_PROPERTY = "language";
    private static final String AUDIO_ITEMS_PROPERTY = "audioItems";

    private final List<AudioItem> mAudioItems;
    private String mLanguage;
    private Boolean mBargeIn;

    public MessageTurn(String name, AudioItem... audioItems) {
        super(name);
        mAudioItems = Arrays.asList(audioItems);
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public void setBargeIn(Boolean bargeIn) {
        mBargeIn = bargeIn;
    }

    public List<AudioItem> getAudioItems() {
        return mAudioItems;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public Boolean getBargeIn() {
        return mBargeIn;
    }

    @Override
    protected String getOuputTurnType() {
        return "message";
    }

    @Override
    protected JsonValue getTurnAsJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, AUDIO_ITEMS_PROPERTY, JsonUtils.toJson(mAudioItems));
        JsonUtils.add(builder, LANGUAGE_PROPERTY, mLanguage);
        if (mBargeIn == null) {
            builder.addNull(BARGE_IN_PROPERTY);
        } else {
            builder.add(BARGE_IN_PROPERTY, mBargeIn.booleanValue());
        }

        return builder.build();
    }

    @Override
    protected Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {

        Document document = createDocument(dialogueContext, null);
        Element formElement = createForm(document);

        Element blockElement = addBlockElement(formElement);

        createPrompt(mLanguage, blockElement, dialogueContext, mBargeIn, mAudioItems);

        createGotoSubmit(blockElement);
        addSubmitForm(dialogueContext, document, this);
        addFatalErrorHandlerForm(dialogueContext, document, this);

        return document;
    }
}
