/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;
import static java.util.Arrays.*;

import java.util.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A <code>MessageTurn</code> is a <code>VoiceXmlOutputTurn</code> that plays a
 * sequence of <code>AudioItem</code>.
 * 
 * @author Nu Echo Inc.
 * @see AudioItem
 * @see http://www.w3.org/TR/voicexml20/#dml4.1.8
 */
public final class MessageTurn extends VoiceXmlOutputTurn {
    private static final String BARGE_IN_PROPERTY = "bargeIn";
    private static final String LANGUAGE_PROPERTY = "language";
    private static final String AUDIO_ITEMS_PROPERTY = "audioItems";

    private final List<AudioItem> mAudioItems;
    private String mLanguage;
    private Boolean mBargeIn;

    /**
     * @param name The name of this turn. Not empty.
     * @param audioItems The sequence of <code>AudioItem</code> to play. Not
     *            empty.
     */
    public MessageTurn(String name, List<AudioItem> audioItems) {
        super(name);
        Assert.notEmpty(audioItems, "audioItems");
        mAudioItems = new ArrayList<AudioItem>(audioItems);
    }

    /**
     * @param name The name of this turn. Not empty.
     * @param audioItems The sequence of <code>AudioItem</code> to play. Not
     *            empty.
     */
    public MessageTurn(String name, AudioItem... audioItems) {
        this(name, asList(audioItems));
    }

    /**
     * @param language The language identifier for the message. Null reverts to
     *            VoiceXML default value.
     */
    public void setLanguage(String language) {
        mLanguage = language;
    }

    /**
     * @param bargeIn Boolean.TRUE if the message is interruptible,
     *            Boolean.FALSE if it is not. Null reverts to VoiceXML default
     *            value.
     */
    public void setBargeIn(Boolean bargeIn) {
        mBargeIn = bargeIn;
    }

    public List<AudioItem> getAudioItems() {
        return Collections.unmodifiableList(mAudioItems);
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

        addSubmitForm(dialogueContext, document, this);
        createGotoSubmit(blockElement);

        return document;
    }
}