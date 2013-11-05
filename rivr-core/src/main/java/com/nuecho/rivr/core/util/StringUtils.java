/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

import java.util.*;
import java.util.regex.*;

/**
 * @author Nu Echo Inc.
 */
public final class StringUtils {

    private StringUtils() {
        //utility class: instantiation forbidden
    }

    public static String join(Iterable<?> list, String delimiter) {
        StringBuilder builder = new StringBuilder();

        int index = 0;
        for (Object item : list) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(String.valueOf(item));
            index++;
        }

        return builder.toString();
    }

    public static String join(Object[] items, String delimiter) {
        StringBuilder builder = new StringBuilder();
        join(builder, items, delimiter);
        return builder.toString();
    }

    public static void join(StringBuilder builder, Object[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static void join(StringBuilder builder, boolean[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static void join(StringBuilder builder, char[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static void join(StringBuilder builder, double[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static void join(StringBuilder builder, float[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static void join(StringBuilder builder, long[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static void join(StringBuilder builder, int[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static void join(StringBuilder builder, short[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static void join(StringBuilder builder, byte[] items, String delimiter) {
        for (int index = 0; index < items.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(items[index]);
        }
    }

    public static String[] getAllMessagesAsArray(Throwable throwable) {
        List<String> messages = new ArrayList<String>();

        while (throwable != null) {
            StringBuilder message = new StringBuilder();

            message.append("(");
            message.append(throwable.getClass().getName());
            message.append(") ");

            if (throwable.getMessage() != null) {
                message.append(throwable.getMessage());
            } else {
                message.append("[no message]");
            }
            messages.add(message.toString());
            throwable = throwable.getCause();
        }

        return messages.toArray(new String[messages.size()]);
    }

    public static String getAllMessages(Throwable throwable) {
        return join(getAllMessagesAsArray(throwable), "\n");
    }

    /**
     * Create a lowercase hexadecimal {@link String} representing a
     * <code>byte</code> array.
     * 
     * @param bytes the <code>byte</code> array to be expressed
     * @return the resulting {@link String}, in which each input byte has
     *         been turned into two hexadecimal characters.
     */
    public static String bytesToHex(byte[] bytes) {
        char[] buffer = new char[2 * (bytes.length)];
        int cursor = 0;

        for (byte b : bytes) {
            int nibble1 = 0x0F & b >> 4;
            int nibble2 = 0x0F & b;
            buffer[cursor++] = (char) (nibble1 < 10 ? nibble1 + '0' : nibble1 - 10 + 'a');
            buffer[cursor++] = (char) (nibble2 < 10 ? nibble2 + '0' : nibble2 - 10 + 'a');
        }

        return new String(buffer);
    }

    public static String replaceAll(Pattern pattern, String string, String replacement) {
        return pattern.matcher(string).replaceAll(replacement);
    }
}