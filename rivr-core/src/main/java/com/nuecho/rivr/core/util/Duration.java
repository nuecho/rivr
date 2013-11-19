/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

import java.io.*;
import java.util.regex.*;

/**
 * Represents a delta of time.
 * 
 * @author Nu Echo Inc.
 */
public final class Duration implements Comparable<Duration>, Serializable {

    /* Even though they fit inside an integer, those constant should be kept as long to avoid 
     * potential mistakes (overflow) when doing arithmetics with them. By having them in a long,
     * we avoid forgetful people who might do WEEK_IN_MILLIS * 1000 instead of WEEK_IN_MILLIS * 1000L
     */
    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;
    public static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
    public static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;
    public static final long WEEK_IN_MILLIS = 7 * DAY_IN_MILLIS;
    public static final long YEAR_IN_MILLIS = DAY_IN_MILLIS * 365L + HOUR_IN_MILLIS * 6;

    private static final long serialVersionUID = 1L;

    public static final Duration ZERO = new Duration(0);
    public static final Pattern DURATION_EXPRESSION_REGEXP = Pattern.compile("((\\d+)\\s*y)?\\s*"
                                                                             + "((\\d+)\\s*d)?\\s*"
                                                                             + "((\\d+)\\s*h)?\\s*"
                                                                             + "((\\d+)\\s*m)?\\s*"
                                                                             + "((\\d+)\\s*s)?\\s*"
                                                                             + "((\\d+)\\s*ms)?");

    public static Duration seconds(long seconds) {
        Assert.between(0, seconds, Long.MAX_VALUE / SECOND_IN_MILLIS);
        return new Duration(seconds * SECOND_IN_MILLIS);
    }

    public static Duration minutes(long minutes) {
        Assert.between(0, minutes, Long.MAX_VALUE / MINUTE_IN_MILLIS);
        return new Duration(minutes * MINUTE_IN_MILLIS);
    }

    public static Duration hours(long hours) {
        Assert.between(0, hours, Long.MAX_VALUE / HOUR_IN_MILLIS);
        return new Duration(hours * HOUR_IN_MILLIS);
    }

    public static Duration days(long days) {
        Assert.between(0, days, Long.MAX_VALUE / DAY_IN_MILLIS);
        return new Duration(days * DAY_IN_MILLIS);
    }

    public static Duration year(long year) {
        Assert.between(0, year, Long.MAX_VALUE / YEAR_IN_MILLIS);
        return new Duration(year * YEAR_IN_MILLIS);
    }

    public static Duration milliseconds(long milliseconds) {
        Assert.notNegative(milliseconds, "milliseconds");
        return new Duration(milliseconds);
    }

    public static Duration parse(String text) {
        Duration duration = Duration.ZERO;

        text = text.trim();
        Matcher matcher = DURATION_EXPRESSION_REGEXP.matcher(text);

        if (!matcher.matches()) throw new IllegalArgumentException("Invalid time value syntax: [" + text + "]");

        String yearsMatch = matcher.group(2);
        if (yearsMatch != null) {
            duration = Duration.sum(duration, Duration.year(Long.parseLong(yearsMatch)));
        }

        String daysMatch = matcher.group(4);
        if (daysMatch != null) {
            duration = Duration.sum(duration, Duration.days(Long.parseLong(daysMatch)));
        }

        String hoursMatch = matcher.group(6);
        if (hoursMatch != null) {
            duration = Duration.sum(duration, Duration.hours(Long.parseLong(hoursMatch)));
        }

        String minutesMatch = matcher.group(8);
        if (minutesMatch != null) {
            duration = Duration.sum(duration, Duration.minutes(Long.parseLong(minutesMatch)));
        }

        String secondsMatch = matcher.group(10);
        if (secondsMatch != null) {
            duration = Duration.sum(duration, Duration.seconds(Long.parseLong(secondsMatch)));
        }

        String millisecondsMatch = matcher.group(12);
        if (millisecondsMatch != null) {
            duration = Duration.sum(duration, Duration.milliseconds(Long.parseLong(millisecondsMatch)));
        }
        return duration;
    }

    public static Duration sum(Duration a, Duration b) {
        long millisecondsA = a.getMilliseconds();
        long millisecondsB = b.getMilliseconds();

        long allowedMax = Long.MAX_VALUE - millisecondsA;
        if (millisecondsB > allowedMax) throw new AssertionError("Time sum would overflow Long capacity.");

        return new Duration(millisecondsA + millisecondsB);
    }

    private final long mMilliseconds;

    private Duration(long milliseconds) {
        mMilliseconds = milliseconds;
    }

    public long getMilliseconds() {
        return mMilliseconds;
    }

    @Override
    public int compareTo(Duration other) {
        // Don't simply return (int) mMilliseconds - other.mMilliseconds because of potential int overflow.
        long diff = mMilliseconds - other.mMilliseconds;
        if (diff < 0) return -1;
        else if (diff > 0) return 1;
        return 0;
    }

    @Override
    public String toString() {
        if (mMilliseconds == 0) return "0 ms";

        StringBuffer buffer = new StringBuffer();

        long value = append(buffer, YEAR_IN_MILLIS, "year", mMilliseconds, true, true);
        value = append(buffer, DAY_IN_MILLIS, "day", value, true, true);
        value = append(buffer, HOUR_IN_MILLIS, "hour", value, true, true);
        value = append(buffer, MINUTE_IN_MILLIS, "minute", value, true, true);
        value = append(buffer, SECOND_IN_MILLIS, "second", value, true, true);
        append(buffer, 1, "millisecond", value, true, true);

        if (mMilliseconds > 1000) {
            buffer.append("(");
            buffer.append(mMilliseconds);
            buffer.append(" ms)");
        }
        return buffer.toString();
    }

    private long append(StringBuffer buffer,
                        long constant,
                        String label,
                        long baseValue,
                        boolean appendPlural,
                        boolean appendWhitespace) {
        long value = baseValue / constant;
        if (value == 0) return baseValue;
        buffer.append(value);

        if (appendWhitespace) {
            buffer.append(" ");
        }
        buffer.append(label);
        if (value > 1 && appendPlural) {
            buffer.append("s");
        }
        if (appendWhitespace) {
            buffer.append(" ");
        }
        return baseValue % constant;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (mMilliseconds ^ mMilliseconds >>> 32);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Duration other = (Duration) obj;
        if (mMilliseconds != other.mMilliseconds) return false;
        return true;
    }

}