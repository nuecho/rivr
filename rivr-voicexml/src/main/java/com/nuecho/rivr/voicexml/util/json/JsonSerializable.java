/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.util.json;

import javax.json.*;

/**
 * Indicates that the class intances can be converted to a {@link JsonValue JSON
 * value}.
 * 
 * @author Nu Echo Inc.
 */
public interface JsonSerializable {
    JsonValue asJson();
}
