/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.util.json;

import java.io.*;
import java.util.*;

import javax.json.*;
import javax.json.spi.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;

/**
 * @author Nu Echo Inc.
 */
public final class JsonUtils {

    /**
     * @author Nu Echo Inc.
     */
    private static final class JsonStringImplementation implements JsonString {
        private final String mString;

        private JsonStringImplementation(String string) {
            mString = string;
        }

        @Override
        public ValueType getValueType() {
            return ValueType.STRING;
        }

        @Override
        public String getString() {
            return mString;
        }

        @Override
        public CharSequence getChars() {
            return mString;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mString == null) ? 0 : mString.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            JsonStringImplementation other = (JsonStringImplementation) obj;
            if (mString == null) {
                if (other.mString != null) return false;
            } else if (!mString.equals(other.mString)) return false;
            return true;
        }

    }

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
        return new JsonStringImplementation(string);
    }

    public static void addTimeProperty(JsonObjectBuilder builder, String propertyName, TimeValue timeValue) {
        if (timeValue == null) {
            builder.addNull(propertyName);
        } else {
            builder.add(propertyName, timeValue.getMilliseconds());
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

    public static JsonValue toJson(List<? extends JsonSerializable> serializables) {
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

        Writer filterWriter = new FilterWriter(writer) {
            @Override
            public void close() throws IOException {
                //no-op.  jsonWriter.close() would close the underlying stream otherwise.
            }
        };

        JsonWriter jsonWriter = createWriter(filterWriter);
        jsonWriter.write(structure);
        jsonWriter.close();
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
