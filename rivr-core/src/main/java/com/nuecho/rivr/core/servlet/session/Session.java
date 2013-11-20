/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet.session;

import javax.servlet.http.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.channel.synchronous.*;
import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class Session<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>>
        implements DialogueChannelListener<I, O> {
    private SynchronousDialogueChannel<I, O, F, L, C> mDialogueChannel;

    private C mDialogueContext;

    private final SessionContainer<I, O, F, L, C> mContainer;
    private final String mId;
    private HttpSession mAssociatedHttpSession;

    public Session(SessionContainer<I, O, F, L, C> container, String sessionId) {
        mContainer = container;
        mId = sessionId;
    }

    @Override
    public void onStart(DialogueChannel<I, O> dialogueChannel) {}

    @Override
    public void onStop(DialogueChannel<I, O> dialogueChannel) {
        stop();
    }

    public void stop() {
        if (mDialogueChannel != null && mDialogueChannel.isDialogueActive()) {
            mDialogueChannel.stop();
        }

        mContainer.removeSession(mId);

        if (mAssociatedHttpSession != null) {
            mAssociatedHttpSession.invalidate();
            mAssociatedHttpSession = null;
        }
    }

    public void keepAlive() {
        if (mAssociatedHttpSession != null) {
            mAssociatedHttpSession.getAttributeNames();
        }
    }

    public String getId() {
        return mId;
    }

    public SynchronousDialogueChannel<I, O, F, L, C> getDialogueChannel() {
        return mDialogueChannel;
    }

    public void setDialogueChannel(SynchronousDialogueChannel<I, O, F, L, C> dialogueChannel) {
        mDialogueChannel = dialogueChannel;
        mDialogueChannel.addListener(this);
    }

    public C getDialogueContext() {
        return mDialogueContext;
    }

    public void setDialogueContext(C dialogueContext) {
        mDialogueContext = dialogueContext;
    }

    public void setAssociatedHttpSession(HttpSession associatedHttpSession) {
        mAssociatedHttpSession = associatedHttpSession;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendItem("mDialogueChannel", mDialogueChannel);
        builder.appendItem("mDialogueContext", mDialogueContext);
        builder.appendItem("mId", mId);
        return builder.getString();
    }
}