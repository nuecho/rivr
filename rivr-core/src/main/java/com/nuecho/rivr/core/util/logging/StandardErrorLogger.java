/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util.logging;

import java.io.*;

/**
 * @author Nu Echo Inc.
 */
public class StandardErrorLogger extends ConsoleLogger {

    public StandardErrorLogger(LogLevel logLevel) {
        super(logLevel);
    }

    @Override
    protected PrintStream getPrintStream() {
        return System.err;
    }

    @Override
    public String getName() {
        return "Standard Error";
    }

}
