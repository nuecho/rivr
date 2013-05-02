/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util.logging;

import java.io.*;

/**
 * @author Nu Echo Inc.
 */
public class StandardOutputLogger extends ConsoleLogger {

    public StandardOutputLogger(LogLevel logLevel) {
        super(logLevel);
    }

    @Override
    protected PrintStream getPrintStream() {
        return System.out;
    }

    @Override
    public String getName() {
        return "Standard Output";
    }

}
