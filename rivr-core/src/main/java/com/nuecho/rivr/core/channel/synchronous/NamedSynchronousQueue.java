/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

/**
 * @author Nu Echo Inc.
 */
package com.nuecho.rivr.core.channel.synchronous;

import java.util.concurrent.*;

public class NamedSynchronousQueue<E> extends SynchronousQueue<E> {

    private static final long serialVersionUID = 6507522115811287461L;

    private final String mName;

    public NamedSynchronousQueue(String name, boolean fair) {
        super(fair);
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
