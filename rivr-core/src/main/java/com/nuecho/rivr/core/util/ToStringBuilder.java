/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

/**
 * @author Nu Echo Inc.
 */
public final class ToStringBuilder {

    private final StringBuilder mBuffer = new StringBuilder();
    private boolean mEmittedProperty;

    public ToStringBuilder(Object object) {
        String className = object.getClass().getName();
        mBuffer.append(className.substring(className.lastIndexOf('.') + 1));
        mBuffer.append(" [");
    }

    public ToStringBuilder appendItem(String propertyName, Object propertyValue) {
        if (propertyValue != null) {
            Class<?> propertyClass = propertyValue.getClass();
            if (propertyClass.isArray()) {
                Class<?> componentType = propertyClass.getComponentType();
                if (!componentType.isPrimitive()) {
                    Object[] array = (Object[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                } else if (componentType == Boolean.TYPE) {
                    boolean[] array = (boolean[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                } else if (componentType == Character.TYPE) {
                    char[] array = (char[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                } else if (componentType == Double.TYPE) {
                    double[] array = (double[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                } else if (componentType == Float.TYPE) {
                    float[] array = (float[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                } else if (componentType == Long.TYPE) {
                    long[] array = (long[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                } else if (componentType == Integer.TYPE) {
                    int[] array = (int[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                } else if (componentType == Short.TYPE) {
                    short[] array = (short[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                } else if (componentType == Byte.TYPE) {
                    byte[] array = (byte[]) propertyValue;
                    insertPropertyName(propertyName, true);
                    StringUtils.join(mBuffer, array, ", ");
                    close(true);
                }
            } else {
                add(propertyName, String.valueOf(propertyValue), false);
            }
        } else {
            add(propertyName, "null", false);
        }
        return this;
    }

    public ToStringBuilder appendItem(String propertyName, long propertyValue) {
        add(propertyName, String.valueOf(propertyValue), false);
        return this;
    }

    public ToStringBuilder appendItem(String propertyName, int propertyValue) {
        add(propertyName, String.valueOf(propertyValue), false);
        return this;
    }

    public ToStringBuilder appendItem(String propertyName, double propertyValue) {
        add(propertyName, String.valueOf(propertyValue), false);
        return this;
    }

    public ToStringBuilder appendItem(String propertyName, float propertyValue) {
        add(propertyName, String.valueOf(propertyValue), false);
        return this;
    }

    public ToStringBuilder appendItem(String propertyName, short propertyValue) {
        add(propertyName, String.valueOf(propertyValue), false);
        return this;
    }

    public ToStringBuilder appendItem(String propertyName, byte propertyValue) {
        add(propertyName, String.valueOf(propertyValue), false);
        return this;
    }

    public ToStringBuilder appendItem(String propertyName, boolean propertyValue) {
        add(propertyName, String.valueOf(propertyValue), false);
        return this;
    }

    public ToStringBuilder appendItem(String propertyName, char propertyValue) {
        add(propertyName, String.valueOf(propertyValue), false);
        return this;
    }

    private void add(String propertyName, String propertyValue, boolean withBraces) {
        insertPropertyName(propertyName, withBraces);
        mBuffer.append(propertyValue);
        close(withBraces);
    }

    private void insertPropertyName(String propertyName, boolean withBraces) {
        if (mEmittedProperty) {
            mBuffer.append(", ");
        }

        if (propertyName.startsWith("m") && propertyName.length() > 1 && Character.isUpperCase(propertyName.charAt(1))) {
            propertyName = propertyName.substring(1); //strip prefix m
        }

        //down-case first letter
        StringBuffer processedName = new StringBuffer(propertyName);
        processedName.setCharAt(0, Character.toLowerCase(propertyName.charAt(0)));
        propertyName = processedName.toString();

        mBuffer.append(propertyName);
        mBuffer.append("=");

        if (withBraces) {
            mBuffer.append("{");
        }

    }

    private void close(boolean withBraces) {
        if (withBraces) {
            mBuffer.append("}");
        }

        mEmittedProperty = true;
    }

    public String getString() {
        mBuffer.append("]");
        return mBuffer.toString();
    }
}