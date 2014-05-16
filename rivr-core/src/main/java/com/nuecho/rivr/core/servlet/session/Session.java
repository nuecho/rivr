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
 * Contains everything that is required for the dialogue to run in a
 * {@link DialogueServlet} controller.
 * <p>
 * NOTE: a Rivr {@link Session} can be linked with an {@link HttpSession}. This
 * can be useful to maintain server stickyness in a clustered environment with
 * load balancers. Web container generates JSESSIONID cookies for session
 * tracking purpose but this information is also used by load balancer equipment
 * between the HTTP user agent and the server to preserve server stickyness.
 * 
 * @param <F> type of {@link FirstTurn}
 * @param <L> type of {@link LastTurn}
 * @param <O> type of {@link OutputTurn}
 * @param <I> type of {@link InputTurn}
 * @param <C> type of {@link DialogueContext}
 * @see DialogueServlet
 * @see SessionContainer
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

    public synchronized void stop() {
        if (mDialogueChannel != null && mDialogueChannel.isDialogueActive()) {
            mDialogueChannel.stop();
        }

        mContainer.removeSession(mId);

        if (mAssociatedHttpSession != null) {
            try {
                mAssociatedHttpSession.invalidate();
            } catch (IllegalStateException exception) {
                //already invalidated
            }
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