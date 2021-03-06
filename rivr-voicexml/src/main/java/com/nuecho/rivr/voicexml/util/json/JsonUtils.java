/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.util.json;

import java.io.*;

import javax.json.*;
import javax.json.spi.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;

/**
 * JSON-related helper methods.
 * 
 * @author Nu Echo Inc.
 */
public final class JsonUtils {

    private JsonUtils() {
        //utility class: instantiation forbidden
    }

    private static final JsonProvider PROVIDER = JsonProvider.provider();

    public static JsonArrayBuilder createArrayBuilder() {
        return PROVIDER.createArrayBuilder();
    }

    public static JsonObjectBuilder createObjectBuilder() {
        return PROVIDER.createObjectBuilder();
    }

    public static JsonString wrap(final String string) {
        return Json.createArrayBuilder().add(string).build().getJsonString(0);
    }

    public static void addDurationProperty(JsonObjectBuilder builder, String propertyName, Duration duration) {
        if (duration == null) {
            builder.addNull(propertyName);
        } else {
            builder.add(propertyName, duration.getMilliseconds());
        }
    }

    public static void addBooleanProperty(JsonObjectBuilder builder, String propertyName, Boolean value) {
        if (value == null) {
            builder.addNull(propertyName);
        } else {
            builder.add(propertyName, value.booleanValue());
        }
    }

    public static void addXmlNodeProperty(JsonObjectBuilder builder, String propertName, String item, Node node) {
        try {
            builder.add(propertName, DomUtils.writeToString(node));
        } catch (IOException exception) {
            builder.add(propertName,
                        "Exception occurred during serialization of "
                                + item
                                + ": "
                                + StringUtils.getAllMessages(exception));
        }
    }

    public static void addNumberProperty(JsonObjectBuilder builder, String propertyName, Number number) {
        if (number == null) {
            builder.addNull(propertyName);
        } else {
            builder.add(propertyName, number.doubleValue());
        }

    }

    public static JsonValue toJson(Iterable<? extends JsonSerializable> serializables) {
        if (serializables == null) return JsonValue.NULL;
        JsonArrayBuilder builder = createArrayBuilder();
        for (JsonSerializable jsonSerializable : serializables) {
            builder.add(jsonSerializable.asJson());
        }
        return builder.build();
    }

    public static void add(JsonObjectBuilder builder, String name, JsonSerializable serializable) {
        if (serializable == null) {
            builder.addNull(name);
        } else {
            builder.add(name, serializable.asJson());
        }
    }

    public static long getLongProperty(JsonObject object, String property) {
        return object.getJsonNumber(property).longValue();
    }

    public static JsonReader createReader(InputStream inputStream) {
        return PROVIDER.createReader(inputStream);
    }

    public static JsonReader createReader(Reader reader) {
        return PROVIDER.createReader(reader);
    }

    public static JsonReader createReader(String string) {
        return createReader(new StringReader(string));
    }

    public static JsonWriter createWriter(OutputStream outputStream) {
        return PROVIDER.createWriter(outputStream);
    }

    public static JsonWriter createWriter(Writer writer) {
        return PROVIDER.createWriter(writer);
    }

    public static void write(Writer writer, JsonStructure structure) {
        JsonWriter jsonWriter = createWriter(writer);
        jsonWriter.write(structure);
    }

    public static void add(JsonObjectBuilder builder, String property, String string) {
        if (string == null) {
            builder.addNull(property);
        } else {
            builder.add(property, string);
        }
    }

    public static void add(JsonObjectBuilder builder, String property, JsonValue value) {
        if (value == null) {
            builder.addNull(property);
        } else {
            builder.add(property, value);
        }
    }

    public static void add(JsonObjectBuilder builder, String property, JsonArrayBuilder builderToAdd) {
        if (builderToAdd == null) {
            builder.addNull(property);
        } else {
            builder.add(property, builderToAdd);
        }
    }

    public static void add(JsonObjectBuilder builder, String property, JsonObjectBuilder builderToAdd) {
        if (builderToAdd == null) {
            builder.addNull(property);
        } else {
            builder.add(property, builderToAdd);
        }
    }

    public static void add(JsonArrayBuilder builder, JsonValue value) {
        if (value == null) {
            builder.addNull();
        } else {
            builder.add(value);
        }
    }

}
