/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

import java.io.*;
import java.util.regex.*;

/**
 * @author Nu Echo Inc.
 */
public final class TimeValue implements Comparable<TimeValue>, Serializable {

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

    public static final TimeValue ZERO = new TimeValue(0);
    public static final Pattern TIME_EXPRESSION_REGEXP = Pattern.compile("((\\d+)\\s*y)?\\s*"
                                                                         + "((\\d+)\\s*d)?\\s*"
                                                                         + "((\\d+)\\s*h)?\\s*"
                                                                         + "((\\d+)\\s*m)?\\s*"
                                                                         + "((\\d+)\\s*s)?\\s*"
                                                                         + "((\\d+)\\s*ms)?");

    public static TimeValue seconds(long seconds) {
        Assert.between(0, seconds, Long.MAX_VALUE / SECOND_IN_MILLIS);
        return new TimeValue(seconds * SECOND_IN_MILLIS);
    }

    public static TimeValue minutes(long minutes) {
        Assert.between(0, minutes, Long.MAX_VALUE / MINUTE_IN_MILLIS);
        return new TimeValue(minutes * MINUTE_IN_MILLIS);
    }

    public static TimeValue hours(long hours) {
        Assert.between(0, hours, Long.MAX_VALUE / HOUR_IN_MILLIS);
        return new TimeValue(hours * HOUR_IN_MILLIS);
    }

    public static TimeValue days(long days) {
        Assert.between(0, days, Long.MAX_VALUE / DAY_IN_MILLIS);
        return new TimeValue(days * DAY_IN_MILLIS);
    }

    public static TimeValue year(long year) {
        Assert.between(0, year, Long.MAX_VALUE / YEAR_IN_MILLIS);
        return new TimeValue(year * YEAR_IN_MILLIS);
    }

    public static TimeValue milliseconds(long milliseconds) {
        Assert.notNegative(milliseconds, "milliseconds");
        return new TimeValue(milliseconds);
    }

    public static TimeValue parse(String text) {
        TimeValue timeValue = TimeValue.ZERO;

        text = text.trim();
        Matcher matcher = TIME_EXPRESSION_REGEXP.matcher(text);

        if (!matcher.matches()) throw new IllegalArgumentException("Invalid time value syntax: [" + text + "]");

        String yearsMatch = matcher.group(2);
        if (yearsMatch != null) {
            timeValue = TimeValue.sum(timeValue, TimeValue.year(Long.parseLong(yearsMatch)));
        }

        String daysMatch = matcher.group(4);
        if (daysMatch != null) {
            timeValue = TimeValue.sum(timeValue, TimeValue.days(Long.parseLong(daysMatch)));
        }

        String hoursMatch = matcher.group(6);
        if (hoursMatch != null) {
            timeValue = TimeValue.sum(timeValue, TimeValue.hours(Long.parseLong(hoursMatch)));
        }

        String minutesMatch = matcher.group(8);
        if (minutesMatch != null) {
            timeValue = TimeValue.sum(timeValue, TimeValue.minutes(Long.parseLong(minutesMatch)));
        }

        String secondsMatch = matcher.group(10);
        if (secondsMatch != null) {
            timeValue = TimeValue.sum(timeValue, TimeValue.seconds(Long.parseLong(secondsMatch)));
        }

        String millisecondsMatch = matcher.group(12);
        if (millisecondsMatch != null) {
            timeValue = TimeValue.sum(timeValue, TimeValue.milliseconds(Long.parseLong(millisecondsMatch)));
        }
        return timeValue;
    }

    public static TimeValue sum(TimeValue a, TimeValue b) {
        long millisecondsA = a.getMilliseconds();
        long millisecondsB = b.getMilliseconds();

        long allowedMax = Long.MAX_VALUE - millisecondsA;
        if (millisecondsB > allowedMax) throw new AssertionError("Time sum would overflow Long capacity.");

        return new TimeValue(millisecondsA + millisecondsB);
    }

    private final long mMilliseconds;

    private TimeValue(long milliseconds) {
        mMilliseconds = milliseconds;
    }

    public long getMilliseconds() {
        return mMilliseconds;
    }

    @Override
    public int compareTo(TimeValue other) {
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
        TimeValue other = (TimeValue) obj;
        if (mMilliseconds != other.mMilliseconds) return false;
        return true;
    }

}