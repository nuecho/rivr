/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

import java.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class Assert {

    private Assert() {
        //utility class: instantiation forbidden
    }

    public static void notNull(Object item, String symbol) {
        if (item == null) throw new AssertionError(symbol + " should not be null");
    }

    public static void notEmpty(String string, String symbol) {
        notNull(string, symbol);
        ensure(!string.isEmpty(), symbol);
    }

    public static void notEmpty(Collection<?> collection, String symbol) {
        notNull(collection, symbol);
        ensure(!collection.isEmpty(), symbol);
    }

    public static void between(long min, long value, long max) {
        if (value < min || value > max)
            throw new AssertionError("Value should not be in the range [" + min + ", " + max + "]");
    }

    public static void between(double min, double value, double max) {
        if (value < min || value > max)
            throw new AssertionError("Value should not be in the range [" + min + ", " + max + "]");
    }

    public static void notNegative(long value, String symbol) {
        ensure(value >= 0, symbol + " should not be negative. Value=" + value);
    }

    public static void positive(long value, String symbol) {
        ensure(value > 0, symbol + " should be positive. Value=" + value);
    }

    public static void ensure(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

}