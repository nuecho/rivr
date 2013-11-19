/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.channel.synchronous;

import com.nuecho.rivr.core.channel.*;

/**
 * Exception used to indicate that the exchange of turns cannot occur since the
 * {@link DialogueChannel} has been stopped.
 * 
 * @author Nu Echo Inc.
 */
public class DialogueChannelStopped extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DialogueChannelStopped() {
        super();
    }

    public DialogueChannelStopped(String message, Throwable cause) {
        super(message, cause);
    }

    public DialogueChannelStopped(String message) {
        super(message);
    }

    public DialogueChannelStopped(Throwable cause) {
        super(cause);
    }

}
