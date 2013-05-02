/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.rendering.voicexml;

public final class SubmitMethod {

    public static final SubmitMethod POST = new SubmitMethod(VoiceXmlDomUtil.POST_METHOD);
    public static final SubmitMethod GET = new SubmitMethod(VoiceXmlDomUtil.GET_METHOD);

    private final String mValue;

    private SubmitMethod(String elementName) {
        mValue = elementName;
    }

    public String getValue() {
        return mValue;
    }
}