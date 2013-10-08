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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mInitialValueExpression == null) ? 0 : mInitialValueExpression.hashCode());
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VariableDeclaration other = (VariableDeclaration) obj;
        if (mInitialValueExpression == null) {
            if (other.mInitialValueExpression != null) return false;
        } else if (!mInitialValueExpression.equals(other.mInitialValueExpression)) return false;
        if (mName == null) {
            if (other.mName != null) return false;
        } else if (!mName.equals(other.mName)) return false;
        return true;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendItem("mName", mName);
        builder.appendItem("mInitialValueExpression", mInitialValueExpression);
        return builder.getString();
    }

}
