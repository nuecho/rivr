/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

import java.util.*;

/**
 * Assertion facilities.
 * 
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
        ensure(!string.isEmpty(), symbol + " should not be empty.");
    }

    public static void notEmpty(Collection<?> collection, String symbol) {
        notNull(collection, symbol);
        ensure(!collection.isEmpty(), symbol + " should not be empty.");
    }

    public static void noNullValues(Collection<?> collection, String symbol) {
        Assert.notNull(collection, symbol);
        for (Object item : collection) {
            ensure(item != null, symbol + " should not contain null values.");
        }
    }

    public static void noNullValues(Object[] array, String symbol) {
        Assert.notNull(array, symbol);
        int index = 0;
        for (Object item : array) {
            ensure(item != null, symbol + "[" + index + "]  should not be null.");
            index++;
        }
    }

    public static void noNullValues(List<?> list, String symbol) {
        Assert.notNull(list, symbol);
        int index = 0;
        for (Object item : list) {
            ensure(item != null, "item #" + index + " of " + symbol + " should not be null.");
            index++;
        }
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