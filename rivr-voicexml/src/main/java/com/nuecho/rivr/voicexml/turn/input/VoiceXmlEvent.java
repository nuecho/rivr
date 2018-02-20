/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.input;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * A VoiceXml event thrown by the platform composed of a name and a message.
 * 
 * @author Nu Echo Inc.
 * @see <a
 *      href="http://www.w3.org/TR/voicexml20/#dml5.2.6">http://www.w3.org/TR/voicexml20/#dml5.2.6</a>
 */
public final class VoiceXmlEvent implements JsonSerializable {

    private static final String MESSAGE_PROPERT = "message";
    private static final String NAME_PROPERTY = "name";

    private final String mName;
    private final String mMessage;

    public static final String CANCEL = "cancel";
    public static final String CONNECTION_DISCONNECT = "connection.disconnect";
    public static final String CONNECTION_DISCONNECT_HANGUP = "connection.disconnect.hangup";
    public static final String CONNECTION_DISCONNECT_TRANSFER = "connection.disconnect.transfer";
    public static final String EXIT = "exit";
    public static final String HELP = "help";
    public static final String NO_INPUT = "noinput";
    public static final String NO_MATCH = "nomatch";
    public static final String MAX_SPEECH_TIMEOUT = "maxspeechtimeout";
    public static final String ERROR = "error";
    public static final String ERROR_BAD_FETCH = "error.badfetch";
    public static final String ERROR_BAD_FETCH_HTTP = "error.badfetch.http";
    public static final String ERROR_BAD_FETCH_PROTOCOL = "error.badfetch.protocol";
    public static final String ERROR_SEMANTIC = "error.semantic";
    public static final String ERROR_NO_AUTHORIZATION = "error.noauthorization";
    public static final String ERROR_NO_RESOURCE = "error.noresource";
    public static final String ERROR_UNSUPPORTED_BUILTIN = "error.unsupported.builtin";
    public static final String ERROR_UNSUPPORTED_FORMAT = "error.unsupported.format";
    public static final String ERROR_UNSUPPORTED_LANGUAGE = "error.unsupported.language";
    public static final String ERROR_UNSUPPORTED_OBJECT_NAME = "error.unsupported.objectname";
    public static final String ERROR_UNSUPPORTED = "error.unsupported";

    public static final String ERROR_CONNECTION = "error.connection";
    public static final String ERROR_CONNECTION_NO_AUTHORIZATION = "error.connection.noauthorization";
    public static final String ERROR_CONNECTION_BAD_DESTINATION = "error.connection.baddestination";
    public static final String ERROR_CONNECTION_NO_ROUTE = "error.connection.noroute";
    public static final String ERROR_CONNECTION_NO_RESOURCE = "error.connection.noresource";
    public static final String ERROR_CONNECTION_PROTOCOL = "error.connection.protocol";
    public static final String ERROR_UNSUPPORTED_TRANSFER_BLIND = "error.unsupported.transfer.blind";
    public static final String ERROR_UNSUPPORTED_TRANSFER_BRIDGE = "error.unsupported.transfer.bridge";
    public static final String ERROR_UNSUPPORTED_TRANSFER_CONSULTATION = "error.unsupported.transfer.consultation";
    public static final String ERROR_UNSUPPORTED_URI = "error.unsupported.uri";

    public static boolean hasEvent(String prefix, Collection<? extends VoiceXmlEvent> events) {
        for (VoiceXmlEvent voiceXmlEvent : events) {
            if (voiceXmlEvent.isSubtypeOf(prefix)) return true;
        }
        return false;
    }

    public static VoiceXmlEvent getEvent(String prefix, Collection<? extends VoiceXmlEvent> events) {
        for (VoiceXmlEvent voiceXmlEvent : events) {
            if (voiceXmlEvent.isSubtypeOf(prefix)) return voiceXmlEvent;
        }
        return null;
    }

    public VoiceXmlEvent(String name, String message) {
        Assert.notEmpty(name, NAME_PROPERTY);
        mName = name;
        mMessage = message;
    }

    public String getName() {
        return mName;
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isSubtypeOf(String prefix) {
        Assert.notNull(prefix, "prefix");
        return mName.equals(prefix) || mName.startsWith(prefix + ".");
    }

    @Override
    public String toString() {
        return asJson().toString();
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, NAME_PROPERTY, mName);
        JsonUtils.add(builder, MESSAGE_PROPERT, mMessage);
        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mMessage == null) ? 0 : mMessage.hashCode());
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VoiceXmlEvent other = (VoiceXmlEvent) obj;
        if (mMessage == null) {
            if (other.mMessage != null) return false;
        } else if (!mMessage.equals(other.mMessage)) return false;
        if (mName == null) {
            if (other.mName != null) return false;
        } else if (!mName.equals(other.mName)) return false;
        return true;
    }

}