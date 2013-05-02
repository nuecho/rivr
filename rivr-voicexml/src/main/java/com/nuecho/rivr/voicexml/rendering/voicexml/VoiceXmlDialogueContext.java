/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.voicexml;

import java.util.*;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;

/**
 * @author Nu Echo Inc.
 */
public final class VoiceXmlDialogueContext implements DialogueContext<VoiceXmlInputTurn, VoiceXmlOutputTurn> {

    private String mLanguage;

    private FetchConfiguration mFetchConfiguration;
    private final Map<String, String> mProperties = new HashMap<String, String>();

    private String mDialogueId;

    private final String mContextPath;
    private final String mServletPath;
    private int mTurnIndex;

    private final DialogueChannel<VoiceXmlInputTurn, VoiceXmlOutputTurn> mDialogueChannel;

    private final Logger mLogger;

    public VoiceXmlDialogueContext(DialogueChannel<VoiceXmlInputTurn, VoiceXmlOutputTurn> dialogueChannel,
                                   Logger logger,
                                   String dialogueId,
                                   String contextPath,
                                   String servletPath) {
        mDialogueChannel = dialogueChannel;
        mLogger = logger;
        mDialogueId = dialogueId;
        mContextPath = contextPath;
        mServletPath = servletPath;
    }

    public void setDialogueId(String dialogueId) {
        mDialogueId = dialogueId;
    }

    @Override
    public String getDialogueId() {
        return mDialogueId;
    }

    public String getContextPath() {
        return mContextPath;
    }

    public String getServletPath() {
        return mServletPath;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public FetchConfiguration getFetchConfiguration() {
        return mFetchConfiguration;
    }

    public void setFetchConfiguration(FetchConfiguration fetchConfiguration) {
        mFetchConfiguration = fetchConfiguration;
    }

    public Map<String, String> getProperties() {
        return mProperties;
    }

    public void incrementTurnIndex() {
        mTurnIndex++;
    }

    public int getTurnIndex() {
        return mTurnIndex;
    }

    @Override
    public DialogueChannel<VoiceXmlInputTurn, VoiceXmlOutputTurn> getDialogueChannel() {
        return mDialogueChannel;
    }

    @Override
    public Logger getLogger() {
        return mLogger;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendItem("mLanguage", mLanguage);
        builder.appendItem("mFetchConfiguration", mFetchConfiguration);
        builder.appendItem("mProperties", mProperties);
        builder.appendItem("mDialogueId", mDialogueId);
        builder.appendItem("mContextPath", mContextPath);
        builder.appendItem("mServletPath", mServletPath);
        builder.appendItem("mTurnIndex", mTurnIndex);
        return builder.getString();
    }

}