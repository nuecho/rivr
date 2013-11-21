/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.servlet.session;

import java.util.*;

import org.slf4j.*;

import com.nuecho.rivr.core.channel.*;
import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;

/**
 * Stores {@link Session} and check for expirations.
 * <p>
 * Rivr has its own SessionContainer independent from the Web server. This
 * solves many issues related to cookies and encoding of session ID in the URI.
 * <p>
 * A clean-up thread checks periodically that every session has not timed-out.
 * The scan period and the session time-out value can be specified in the
 * {@link #SessionContainer(Logger, Duration, Duration, String) constructor},
 * although they are normally specified via the {@link DialogueServlet}.
 * 
 * @see DialogueServlet#setSessionScanPeriod(Duration)
 * @see DialogueServlet#setSessionTimeout(Duration)
 * @author Nu Echo Inc.
 * @param <F> type of {@link FirstTurn}
 * @param <L> type of {@link LastTurn}
 * @param <O> type of {@link OutputTurn}
 * @param <I> type of {@link InputTurn}
 * @param <C> type of {@link DialogueContext}
 */
public final class SessionContainer<I extends InputTurn, O extends OutputTurn, F extends FirstTurn, L extends LastTurn, C extends DialogueContext<I, O>> {

    private final Logger mLogger;
    private final Duration mSessionTimeout;
    private final Duration mTimeoutCheckScanPeriod;
    private final String mName;

    private final Map<String, Session<I, O, F, L, C>> mSessions = new HashMap<String, Session<I, O, F, L, C>>();
    private final Map<String, Long> mLastAccess = new HashMap<String, Long>();
    private boolean mStopped;
    private Thread mTimeoutCheckScanThread;

    public SessionContainer(Logger logger, Duration sessionTimeout, Duration timeoutCheckScanPeriod, String name) {
        mLogger = logger;
        mSessionTimeout = sessionTimeout;
        mTimeoutCheckScanPeriod = timeoutCheckScanPeriod;
        mName = name;
        launchThread();
    }

    private void launchThread() {

        Runnable cleanUpRunnable = new Runnable() {

            @Override
            public void run() {

                while (!mStopped) {
                    try {
                        Iterator<String> sessionIdsIterator = mSessions.keySet().iterator();

                        long currentTime = System.currentTimeMillis();
                        while (sessionIdsIterator.hasNext()) {
                            String sessionId = sessionIdsIterator.next();
                            Long lastAccess = mLastAccess.get(sessionId);
                            if (lastAccess != null) {
                                if (currentTime - lastAccess > mSessionTimeout.getMilliseconds()) {
                                    Session<I, O, F, L, C> sessionContext = mSessions.get(sessionId);
                                    sessionContext.stop();
                                }
                            }
                        }

                        Thread.sleep(mTimeoutCheckScanPeriod.getMilliseconds());
                    } catch (InterruptedException interruptedException) {
                        if (mStopped) {
                            //Interrupts can be swallowed if you know the thread is about to exit
                        } else {
                            Thread.currentThread().interrupt();
                        }
                    } catch (Throwable throwable) {
                        mLogger.error("Error during session time-out check.", throwable);
                    }
                }

                Iterator<String> sessionIdsIterator = mSessions.keySet().iterator();

                //stopping all sessions
                while (sessionIdsIterator.hasNext()) {
                    mSessions.get(sessionIdsIterator.next()).stop();
                }
            }
        };

        mTimeoutCheckScanThread = new Thread(cleanUpRunnable, "Session cleanup thread for " + mName);
        mTimeoutCheckScanThread.setDaemon(true);
        mTimeoutCheckScanThread.start();
    }

    public void addSession(Session<I, O, F, L, C> session) {
        String sessionId = session.getId();
        mSessions.put(sessionId, session);
        updateLastAccessTime(sessionId);
    }

    public void removeSession(String sessionId) {
        mSessions.remove(sessionId);
        mLastAccess.remove(sessionId);
    }

    public Session<I, O, F, L, C> getSession(String sessionId) {
        updateLastAccessTime(sessionId);
        Session<I, O, F, L, C> session = mSessions.get(sessionId);
        if (session != null) {
            session.keepAlive();
        }
        return session;
    }

    private void updateLastAccessTime(String sessionId) {
        mLastAccess.put(sessionId, System.currentTimeMillis());
    }

    public synchronized void stop() {
        if (!mStopped) {
            mStopped = true;
            mTimeoutCheckScanThread.interrupt();
        }
    }

    public Collection<String> getSessionIds() {
        return new HashSet<String>(mSessions.keySet());
    }
}