package com.nuecho.rivr.voicexml.turn.output.interaction;

public enum BargeInType {
    SPEECH("speech"), HOTWORD("hotword");

    private final String mKey;

    BargeInType(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }
}