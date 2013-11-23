/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.util;

import javax.json.*;

import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * Utility class containing helper methods for result returned in some
 * {@link VoiceXmlLastTurn last turns}.
 * 
 * @author Nu Echo Inc.
 */
public class ResultUtils {

    private static final String STACK_TRACE_PROPERTY = "stackTrace";
    private static final String CLASS_PROPERTY = "class";
    private static final String MESSAGE_PROPERTY = "message";

    public static final JsonValue toJson(Throwable throwable) {
        JsonArrayBuilder builder = JsonUtils.createArrayBuilder();
        addThrowable(builder, throwable, null);
        return builder.build();
    }

    private static void addThrowable(JsonArrayBuilder arrayBuilder, Throwable throwable, Throwable parent) {
        JsonObjectBuilder topLevelObjectBuilder = JsonUtils.createObjectBuilder();
        String message = throwable.getMessage();
        if (message != null) {
            JsonUtils.add(topLevelObjectBuilder, MESSAGE_PROPERTY, message);
        }

        JsonUtils.add(topLevelObjectBuilder, CLASS_PROPERTY, throwable.getClass().getName());

        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        JsonArrayBuilder stackTraceBuilder = JsonUtils.createArrayBuilder();

        int framesInCommon;
        if (parent != null) {
            StackTraceElement[] thisStackTrace = stackTraceElements;
            StackTraceElement[] parentStackTrace = parent.getStackTrace();

            int thisStackTraceIndex = thisStackTrace.length - 1;
            int otherStackTraceIndex = parentStackTrace.length - 1;
            while (thisStackTraceIndex >= 0
                   && otherStackTraceIndex >= 0
                   && thisStackTrace[thisStackTraceIndex].equals(parentStackTrace[otherStackTraceIndex])) {
                thisStackTraceIndex--;
                otherStackTraceIndex--;
            }
            framesInCommon = thisStackTrace.length - 1 - thisStackTraceIndex;
        } else {
            framesInCommon = 0;
        }

        int maxFrameIndex = stackTraceElements.length - framesInCommon;

        for (int index = 0; index < maxFrameIndex; index++) {
            StackTraceElement stackTraceElement = stackTraceElements[index];
            stackTraceBuilder.add(stackTraceElement.getClassName()
                                  + "."
                                  + stackTraceElement.getMethodName()
                                  + "("
                                  + stackTraceElement.getFileName()
                                  + ":"
                                  + stackTraceElement.getLineNumber()
                                  + ")");
        }
        if (framesInCommon > 0) {
            stackTraceBuilder.add("... " + framesInCommon + " more");
        }

        JsonUtils.add(topLevelObjectBuilder, STACK_TRACE_PROPERTY, stackTraceBuilder);

        arrayBuilder.add(topLevelObjectBuilder);
        Throwable cause = throwable.getCause();
        if (cause != null) {
            addThrowable(arrayBuilder, cause, throwable);
        }

    }
}