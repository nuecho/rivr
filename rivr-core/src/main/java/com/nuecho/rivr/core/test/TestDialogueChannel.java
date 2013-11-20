/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.core.test;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.channel.synchronous.*;
import com.nuecho.rivr.core.channel.synchronous.step.*;
import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.core.util.logging.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class TestDialogueChannel<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>>
        implements DialogueChannel<I, O> {

    private final SynchronousDialogueChannel<I, O, F, L, C> mChannel;
    private Step<O, L> mLastStep;
    private final String mName;
    private Duration mDefaultTimeout;

    private final AccumulatingLog mAccumulatingLog;
    private Logger mLogger;

    public TestDialogueChannel(String name, Duration defaultTimeout) {
        mName = name;
        mChannel = new SynchronousDialogueChannel<I, O, F, L, C>();
        mAccumulatingLog = new AccumulatingLog(name);
        setLogger(mAccumulatingLog);
        mDefaultTimeout = defaultTimeout;
    }

    @Override
    public I doTurn(O outputTurn, Duration timeout) throws Timeout, InterruptedException {
        return mChannel.doTurn(outputTurn, timeout);
    }

    @Override
    public void addListener(DialogueChannelListener<I, O> listener) {
        mChannel.addListener(listener);
    }

    @Override
    public void removeListener(DialogueChannelListener<I, O> listener) {
        mChannel.removeListener(listener);
    }

    public void getDefaultTimeout(Duration defaultTimeout) {
        mDefaultTimeout = defaultTimeout;
    }

    public void setDefaultTimeout(Duration defaultTimeout) {
        Assert.notNull(defaultTimeout, "defaultTimeout");
        mDefaultTimeout = defaultTimeout;
    }

    public void dispose() {

        if (!mChannel.isDialogueStarted()) throw new AssertionError("The dialogue hasn't run.");

        if (mChannel.isDialogueActive()) {
            stop();
            mLogger.warn("Test done but dialogue still active.");
        }

        mAccumulatingLog.clear();
    }

    public final void stop() {
        if (!mChannel.isDialogueActive()) throw new IllegalStateException("Dialogue not active.");
        try {
            mChannel.stop(Duration.ZERO);
        } catch (InterruptedException exception) {
            mLogger.warn("Interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    public final Step<O, L> startDialogue(Dialogue<I, O, F, L, C> dialogue, F firstTurn, C dialogueContext) {
        return startDialogue(dialogue, firstTurn, mDefaultTimeout, dialogueContext);
    }

    public final Step<O, L> startDialogue(Dialogue<I, O, F, L, C> dialogue,
                                          F firstTurn,
                                          Duration timeout,
                                          C dialogueContext) {
        if (mChannel.isDialogueActive()) throw new IllegalStateException("Dialogue already started.");

        try {
            mLastStep = mChannel.start(dialogue, firstTurn, timeout, dialogueContext);
            return mLastStep;
        } catch (Exception exception) {
            AssertionError assertionError = new AssertionError("Error during start.");
            assertionError.initCause(exception);
            throw assertionError;
        }
    }

    public final Step<O, L> processInputTurn(I inputTurn) {
        return processInputTurn(inputTurn, mDefaultTimeout);
    }

    public final Step<O, L> processInputTurn(I inputTurn, Duration timeout) {

        if (!mChannel.isDialogueActive()) throw new IllegalStateException("Dialogue not started.");

        try {
            mLastStep = mChannel.doTurn(inputTurn, timeout);
            return mLastStep;
        } catch (Exception exception) {
            AssertionError assertionError = new AssertionError("Error during start.");
            assertionError.initCause(exception);
            throw assertionError;
        }
    }

    public Logger getLogger() {
        return mLogger;
    }

    public Step<O, L> getLastStep() {
        return mLastStep;
    }

    public final O getLastStepAsOutputTurn() {
        if (mLastStep instanceof OutputTurnStep) {
            OutputTurnStep<O, L> turnStep = (OutputTurnStep<O, L>) mLastStep;
            return turnStep.getOutputTurn();
        }

        throw new AssertionError("Last output turn was not an output turn step. Last step=" + mLastStep);
    }

    public final L getLastStepAsLastTurn() {
        if (mLastStep instanceof LastTurnStep) {
            LastTurnStep<O, L> turnStep = (LastTurnStep<O, L>) mLastStep;
            return turnStep.getLastTurn();
        }

        throw new AssertionError("Last output turn was not a last turn step. Last step=" + mLastStep);
    }

    public final Throwable getLastStepAsError() {
        if (mLastStep instanceof ErrorStep) {
            ErrorStep<O, L> errorStep = (ErrorStep<O, L>) mLastStep;
            return errorStep.getThrowable();
        }

        throw new AssertionError("Last output turn was not an error step. Last step=" + mLastStep);
    }

    public final SynchronousDialogueChannel<I, O, F, L, C> getChannel() {
        return mChannel;
    }

    public final void dumpLogs() {
        privateDumpLog(prefixLog(new StandardOutputLogger(LogLevel.ALL)));
    }

    public final void dumpLogs(LogLevel logLevel) {
        privateDumpLog(prefixLog(new StandardOutputLogger(logLevel)));
    }

    private void privateDumpLog(Logger consoleLog) {
        mAccumulatingLog.flushTo(consoleLog);
        StackTraceElement callerStackTraceElement = getStackTraceElement(3);
        StackTraceElement currentStackTraceElement = getStackTraceElement(2);
        consoleLog.info(currentStackTraceElement.getMethodName()
                        + "() called at "
                        + callerStackTraceElement.getClassName()
                        + "."
                        + callerStackTraceElement.getMethodName()
                        + "("
                        + callerStackTraceElement.getFileName()
                        + ":"
                        + callerStackTraceElement.getLineNumber()
                        + ")");

        setLogger(consoleLog);
    }

    private void setLogger(Logger log) {
        mLogger = log;
        mChannel.setLogger(mLogger);
    }

    private static StackTraceElement getStackTraceElement(int index) {
        return new Exception().getStackTrace()[index];
    }

    private PrefixedLogger prefixLog(Logger log) {
        return new PrefixedLogger("[" + mName + "] ", log);
    }

}