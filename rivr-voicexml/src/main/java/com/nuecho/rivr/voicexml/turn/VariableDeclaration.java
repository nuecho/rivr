/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.util.*;


/**
 * @author Nu Echo Inc.
 */
public final class VariableDeclaration {

    private final String mName;
    private final String mInitialValueExpression;

    public VariableDeclaration(String name, String initialValueExpression) {
        Assert.notNull(name, "name");
        Assert.ensure(VoiceXmlUtils.isValidIdentifierName(name), "Invalid ECMAScript identifier name: '" + name + "'");
        mName = name;
        mInitialValueExpression = initialValueExpression;
    }

    public VariableDeclaration(String name) {
        this(name, null);
    }

    public String getName() {
        return mName;
    }

    public String getInitialValueExpression() {
        return mInitialValueExpression;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendItem("mName", mName);
        builder.appendItem("mInitialValueExpression", mInitialValueExpression);
        return builder.getString();
    }

}
