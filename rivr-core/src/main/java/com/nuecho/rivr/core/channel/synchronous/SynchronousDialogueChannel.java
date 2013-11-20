/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel.synchronous;

import java.util.*;
import java.util.concurrent.*;

import org.slf4j.*;
import org.slf4j.helpers.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.channel.synchronous.step.*;
import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class SynchronousDialogueChannel<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>>
        implements DialogueChannel<I, O> {

    private NamedSynchronousQueue<Step<O, L>> mFromDialogueToController = new NamedSynchronousQueue<Step<O, L>>("dialogue to controller",
                                                                                                                true);
    private NamedSynchronousQueue<I> mFromControllerToDialogue = new NamedSynchronousQueue<I>("controller to dialogue",
                                                                                              true);
    private Thread mDialogueThread;

    private Duration mSendTimeout = Duration.seconds(5);

    private Duration mDefaultReceiveFromDialogueTimeout = Duration.minutes(1);
    private Duration mDefaultReceiveFromControllerTimeout = Duration.minutes(5);

    private final List<DialogueChannelListener<I, O>> mListener = new ArrayList<DialogueChannelListener<I, O>>();
    private Logger mLogger = NOPLogger.NOP_LOGGER;
    private boolean mStopped;

    private boolean mDialogueStarted;
    private boolean mDialogueDone;

    public Duration getSendTimeout() {
        return mSendTimeout;
    }

    public void setSendTimeout(Duration sendTimeout) {
        Assert.notNull(sendTimeout, "sendTimeout");
        mSendTimeout = sendTimeout;
    }

    public Duration getDefaultReceiveFromDialogueTimeout() {
        return mDefaultReceiveFromDialogueTimeout;
    }

    public void setDefaultReceiveFromDialogueTimeout(Duration defaultReceiveFromDialogueTimeout) {
        Assert.notNull(defaultReceiveFromDialogueTimeout, "defaultReceiveFromDialogueTimeout");
        mDefaultReceiveFromDialogueTimeout = defaultReceiveFromDialogueTimeout;
    }

    public Duration getDefaultReceiveFromControllerTimeout() {
        return mDefaultReceiveFromControllerTimeout;
    }

    public void setDefaultReceiveFromControllerTimeout(Duration defaultReceiveFromControllerTimeout) {
        Assert.notNull(defaultReceiveFromControllerTimeout, "defaultReceiveFromControllerTimeout");
        mDefaultReceiveFromControllerTimeout = defaultReceiveFromControllerTimeout;
    }

    public void setLogger(Logger logger) {
        Assert.notNull(logger, "logger");
        mLogger = logger;
    }

    public Step<O, L> start(final Dialogue<I, O, F, L, C> dialogue,
                            final F firstTurn,
                            Duration timeout,
                            final C context) throws Timeout, InterruptedException {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mDialogueStarted = true;
                for (DialogueChannelListener<I, O> listener : mListener) {
                    listener.onStart(SynchronousDialogueChannel.this);
                }

                Step<O, L> lastStep;
                try {
                    L lastTurn = dialogue.run(firstTurn, context);
                    lastStep = new LastTurnStep<O, L>(lastTurn);
                } catch (Throwable throwable) {
                    mLogger.error("Error in dialogue.", throwable);
                    lastStep = new ErrorStep<O, L>(throwable);
                }

                try {
                    mLogger.trace("Last step: {}", lastStep);
                    send(mFromDialogueToController, lastStep, mSendTimeout);
                } catch (Timeout exception) {
                    throw new RuntimeException("Timeout while sending final result.", exception);
                } catch (InterruptedException exception) {
                    mLogger.info("Dialogue interrupted while sending final result.");
                    Thread.currentThread().interrupt();
                } finally {
                    mDialogueDone = true;
                    mFromDialogueToController = null; // ensure can't receive further output turns from dialogue
                    mFromControllerToDialogue = null; // ensure can't send further input turns to dialogue
                    for (DialogueChannelListener<I, O> listener : mListener) {
                        listener.onStop(SynchronousDialogueChannel.this);
                    }
                    mLogger.info("Dialogue ended.");
                }
            }
        };

        mDialogueThread = new Thread(runnable, "Dialogue " + context.getDialogueId());
        mDialogueThread.start();
        mLogger.info("Dialogue started.");

        return receive(mFromDialogueToController, timeout);
    }

    public boolean isDialogueStarted() {
        return mDialogueStarted;
    }

    public boolean isDialogueDone() {
        return mDialogueDone;
    }

    public boolean isDialogueActive() {
        return mDialogueStarted && !mDialogueDone;
    }

    /**
     * @param timeout Time to wait for dialogue thread to terminate. A value of
     *            Duration.ZERO (or equivalent) means to wait forever.
     */
    public void stop(Duration timeout) throws InterruptedException {
        stop();
        join(timeout);
    }

    public void stop() {
        mStopped = true;
        mDialogueThread.interrupt();
    }

    public void join(Duration timeout) throws InterruptedException {
        mDialogueThread.join(timeout.getMilliseconds());
    }

    @Override
    public I doTurn(O turn, Duration timeout) throws Timeout, InterruptedException {
        verifyState();
        mLogger.trace("OutputTurn: {}", turn);
        OutputTurnStep<O, L> turnStep = new OutputTurnStep<O, L>(turn);
        if (timeout == null) {
            timeout = mDefaultReceiveFromControllerTimeout;
        }
        return exchange(mFromDialogueToController, mFromControllerToDialogue, turnStep, mSendTimeout, timeout);
    }

    public Step<O, L> doTurn(I turn, Duration timeout) throws Timeout, InterruptedException {
        verifyState();
        mLogger.trace("InputTurn: {}", turn);
        if (timeout == null) {
            timeout = mDefaultReceiveFromDialogueTimeout;
        }
        return exchange(mFromControllerToDialogue, mFromDialogueToController, turn, mSendTimeout, timeout);
    }

    private void verifyState() {
        if (mDialogueThread == null) throw new IllegalStateException("Dialogue is not set");
        if (!mDialogueThread.isAlive()) throw new IllegalStateException("Dialogue is not started");
        if (mStopped) throw new IllegalStateException("Dialogue is stopped");
    }

    private <S, R> R exchange(NamedSynchronousQueue<S> sendQueue,
                              NamedSynchronousQueue<R> receiveQueue,
                              S itemToSend,
                              Duration sendTimeout,
                              Duration receiveTimeout) throws Timeout, InterruptedException {
        try {
            send(sendQueue, itemToSend, sendTimeout);
            return receive(receiveQueue, receiveTimeout);
        } catch (InterruptedException interruptedException) {
            if (mStopped) throw new DialogueChannelStopped();
            else throw interruptedException;
        }
    }

    private static <R> R receive(NamedSynchronousQueue<R> receiveQueue, Duration timeout) throws Timeout,
            InterruptedException {
        if (receiveQueue == null) throw new IllegalStateException("Receive queue is closed.");
        R result = receiveQueue.poll(timeout.getMilliseconds(), TimeUnit.MILLISECONDS);
        if (result == null)
            throw new Timeout("Timed-out in receive() after " + timeout + " in [" + receiveQueue.getName() + "]");
        return result;
    }

    private static <S> void send(NamedSynchronousQueue<S> sendQueue, S itemToSend, Duration timeout) throws Timeout,
            InterruptedException {
        if (sendQueue == null) throw new IllegalStateException("Send queue is closed.");
        boolean success = sendQueue.offer(itemToSend, timeout.getMilliseconds(), TimeUnit.MILLISECONDS);
        if (!success) throw new Timeout("Timed-out in send() after " + timeout + " in [" + sendQueue.getName() + "]");
    }

    @Override
    public void addListener(DialogueChannelListener<I, O> listener) {
        mListener.add(listener);
    }

    @Override
    public void removeListener(DialogueChannelListener<I, O> listener) {
        mListener.remove(listener);
    }
}
