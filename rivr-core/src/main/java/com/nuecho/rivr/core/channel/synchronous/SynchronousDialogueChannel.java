/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
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
 * Implementation of {@link DialogueChannel} allowing turns to be apparently
 * exchanged in a regular synchronous fashion while underneath, it is done in
 * asynchronously.
 * <p>
 * This is the most useful implementation of {@link DialogueChannel}. It is used
 * by the {@link DialogueServlet} to handle different HTTP requests occurring in
 * separate threads.
 * <p>
 * When the controller (such as the {@link DialogueServlet}) uses the
 * {@link SynchronousDialogueChannel}, it uses the
 * {@link #start(Dialogue, FirstTurn, Duration, DialogueContext)} method to
 * start the dialogue and the {@link #doTurn(InputTurn, Duration)} method to
 * send input turns to the dialogue. In exchange, the
 * {@link SynchronousDialogueChannel} will return a {@link Step}:
 * <ul>
 * <li>{@link OutputTurnStep}: if the dialogue sends an OutputTurn in response</li>
 * <li>{@link LastTurnStep}: when the dialogue is done</li>
 * <li>{@link ErrorStep}: if an error occurred following the delivery of the
 * {@link InputTurn}</li>
 * </ul>
 * <p>
 * <h3>States</h3> The {@link SynchronousDialogueChannel} has states related to
 * the dialogue:
 * <ul>
 * <li>started</li>
 * <li>done</li>
 * </ul>
 * The {@link #isDialogueStarted()} and {@link #isDialogueDone()} methods
 * correspond to those states. Additionally, the {@link #isDialogueActive()}
 * method tells if the dialogue is <i>started</i> but not yet <i>done</i>.
 * <p>
 * <h3>Time-out values</h3> The {@link SynchronousDialogueChannel} internally
 * keeps two {@link SynchronousQueue SynchronousQueues}:
 * <ul>
 * <li>one for communication of output turn from the {@link Dialogue} to the
 * controller</li>
 * <li>one for communication of input turn from the controller to the
 * {@link Dialogue}</li>
 * </ul>
 * The default timeout values used while waiting from controller or dialogue can
 * be set with {@link #setDefaultReceiveFromControllerTimeout(Duration)} and
 * {@link #setDefaultReceiveFromDialogueTimeout(Duration)}. These properties are
 * used if the <code>timeout</code> parameter is <code>null</code> for the
 * {@link #doTurn(OutputTurn, Duration)},
 * {@link #start(Dialogue, FirstTurn, Duration, DialogueContext)} and
 * {@link #doTurn(InputTurn, Duration)} methods.
 * <p>
 * If not set, <code>defaultReceiveFromControllerTimeout</code> defaults to <b>5
 * minutes</b> and <code>defaultReceiveFromDialogueTimeout</code> defaults to
 * <b>1 minute</b>. This gives 5 minutes for the controller (like the
 * {@link DialogueServlet}) to provide the {@link InputTurn} and 1 minute for
 * the controller to provide the next turn or error before a {@link Timeout}
 * exception is raised.
 * <p>
 * <b>Note:</b> There also exists a less important <code>sendTimeout</code>
 * property which indicates the maximum duration for send operations. A send
 * operation occurs when the controller sends the input turn to the dialogue and
 * when the dialogue send the output turn, the last turn or an error to the
 * controller. Since we expect the dialogue and the controller to already be in
 * a waiting state when the other thread sends the turn, the send operation
 * should not block. For this reason, the default value for this property is
 * <b>5 seconds</b>. This property can be set with the
 * {@link #setSendTimeout(Duration)} property.
 * <p>
 * 
 * @param <F> type of {@link FirstTurn}
 * @param <L> type of {@link LastTurn}
 * @param <O> type of {@link OutputTurn}
 * @param <I> type of {@link InputTurn}
 * @param <C> type of {@link DialogueContext}
 * @see DialogueServlet
 * @see Step
 * @see OutputTurnStep
 * @see LastTurnStep
 * @see ErrorStep
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

    /**
     * Gets the maximum duration for send operations. A send operation occurs
     * when the controller sends the input turn to the dialogue and when the
     * dialogue send the output turn, the last turn or an error to the
     * controller.
     */
    public Duration getSendTimeout() {
        return mSendTimeout;
    }

    /**
     * Sets the maximum duration for send operations. A send operation occurs
     * when the controller sends the input turn to the dialogue and when the
     * dialogue send the output turn, the last turn or an error to the
     * controller. Since we expect the dialogue and the controller to already be
     * in a waiting state when the other thread sends the turn, the send
     * operation should not block. For this reason, the default value for this
     * property is <b>5 seconds</b>.
     * 
     * @param sendTimeout The timeout value. Cannot be <code>null</code>. A
     *            value of Duration.ZERO (or equivalent) means to wait forever.
     */
    public void setSendTimeout(Duration sendTimeout) {
        Assert.notNull(sendTimeout, "sendTimeout");
        mSendTimeout = sendTimeout;
    }

    /**
     * Retrieves the maximum duration for send operations. A send operation
     * occurs when the controller sends the input turn to the dialogue and when
     * the dialogue send the output turn, the last turn or an error to the
     * controller.
     */
    public Duration getDefaultReceiveFromDialogueTimeout() {
        return mDefaultReceiveFromDialogueTimeout;
    }

    /**
     * Sets the maximum duration the controller thread can wait for a turn or an
     * error from the dialogue thread when not specified by the controller. If
     * this method is not called, it defaults to 1 minute.
     * 
     * @param defaultReceiveFromDialogueTimeout The default timeout to use when
     *            not specified by the controller. Cannot be <code>null</code>.
     */
    public void setDefaultReceiveFromDialogueTimeout(Duration defaultReceiveFromDialogueTimeout) {
        Assert.notNull(defaultReceiveFromDialogueTimeout, "defaultReceiveFromDialogueTimeout");
        mDefaultReceiveFromDialogueTimeout = defaultReceiveFromDialogueTimeout;
    }

    /**
     * Gets the maximum duration the controller thread can wait for a turn or an
     * error from the dialogue thread when not specified by the controller.
     */
    public Duration getDefaultReceiveFromControllerTimeout() {
        return mDefaultReceiveFromControllerTimeout;
    }

    /**
     * Sets the maximum duration the dialogue thread can wait for a turn from
     * the controller thread when not specified by the dialogue.
     * 
     * @param defaultReceiveFromControllerTimeout The default timeout to use
     *            when not specified by the dialogue. Cannot be
     *            <code>null</code>.
     */
    public void setDefaultReceiveFromControllerTimeout(Duration defaultReceiveFromControllerTimeout) {
        Assert.notNull(defaultReceiveFromControllerTimeout, "defaultReceiveFromControllerTimeout");
        mDefaultReceiveFromControllerTimeout = defaultReceiveFromControllerTimeout;
    }

    /**
     * Sets the logger for this dialogue channel.
     * 
     * @param logger The logger. Cannot be <code>null</code>.
     */
    public void setLogger(Logger logger) {
        Assert.notNull(logger, "logger");
        mLogger = logger;
    }

    /**
     * Starts a {@link Dialogue} in a new thread.
     * 
     * @param dialogue Dialogue to start. Cannot be <code>null</code>.
     * @param firstTurn First turn used passed to
     *            {@link Dialogue#run(FirstTurn, DialogueContext)} method of the
     *            dialogue. Cannot be <code>null</code>.
     * @param timeout maximum time allowed to receive the turn from the
     *            dialogue. If <code>null</code>, uses the
     *            <code>defaultReceiveFromDialogueTimeout</code> property. A
     *            value of Duration.ZERO (or equivalent) means to wait forever.
     * @param context Dialogue context to pass to
     *            {@link Dialogue#run(FirstTurn, DialogueContext)} method of the
     *            dialogue. Cannot be <code>null</code>.
     * @throws Timeout If no result can be obtain from dialogue after delay
     *             specified by <code>timeout</code> parameter.
     */
    public Step<O, L> start(final Dialogue<I, O, F, L, C> dialogue, final F firstTurn, Duration timeout, final C context)
            throws Timeout, InterruptedException {

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

    /**
     * Tells if the dialogue has started.
     * 
     * @return <code>true</code> if the dialogue has started, <code>false</code>
     *         otherwise.
     */
    public boolean isDialogueStarted() {
        return mDialogueStarted;
    }

    /**
     * Tells if the dialogue has ended.
     * 
     * @return <code>true</code> if the dialogue has ended, <code>false</code>
     *         otherwise.
     */
    public boolean isDialogueDone() {
        return mDialogueDone;
    }

    /**
     * Tells if the dialogue has started but not yet ended.
     * 
     * @return <code>true</code> if the dialogue has started but not ended yet,
     *         <code>false</code> otherwise.
     */
    public boolean isDialogueActive() {
        return mDialogueStarted && !mDialogueDone;
    }

    /**
     * Stops the dialogue and wait for the dialogue thread to end.
     * 
     * @param timeout Time to wait for dialogue thread to terminate. A value of
     *            Duration.ZERO (or equivalent) means to wait forever.
     */
    public void stop(Duration timeout) throws InterruptedException {
        stop();
        join(timeout);
    }

    /**
     * Stops the dialogue.
     */
    public void stop() {
        mStopped = true;
        mDialogueThread.interrupt();
    }

    /**
     * Wait for the dialogue thread to end.
     * 
     * @param timeout maximum time to wait for the thread to end. A value of
     *            Duration.ZERO (or equivalent) means to wait forever.
     */
    public void join(Duration timeout) throws InterruptedException {
        mDialogueThread.join(timeout.getMilliseconds());
    }

    /**
     * Performs a turn exchange: the dialogue channel will return the
     * {@link InputTurn}
     * 
     * @param turn The output turn to send. Cannot be <code>null</code>.
     * @param timeout maximum time allowed to receive the turn from the
     *            dialogue. If <code>null</code>, uses the
     *            <code>defaultReceiveFromControllerTimeout</code>. A value of
     *            Duration.ZERO (or equivalent) means to wait forever.
     * @return the received {@link InputTurn}. Cannot be <code>null</code>.
     * @throws Timeout if the dialogue channel has not been able to give the
     *             InputTurn before <code>timeout</code> parameter.
     * @throws InterruptedException if the thread has been interrupted while
     *             waiting for the result.
     */

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

    /**
     * Performs a turn exchange: the dialogue channel will return the Step
     * 
     * @param turn the input turn to send to the dialogue
     * @param timeout maximum time allowed to receive the turn from the
     *            dialogue. If <code>null</code>, uses the
     *            <code>defaultReceiveFromDialogueTimeout</code> property. A
     *            value of Duration.ZERO (or equivalent) means to wait forever.
     * @throws Timeout If no result can be obtain from dialogue after delay
     *             specified by <code>timeout</code> parameter.
     * @return the {@link Step} wrapping the dialogue next step
     */
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
