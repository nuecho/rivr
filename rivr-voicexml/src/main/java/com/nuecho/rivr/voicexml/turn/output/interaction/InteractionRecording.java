/*
 * Copyright (c) 2002-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.turn.output.interaction;

import static java.util.Arrays.*;

import java.util.*;

import javax.json.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class InteractionRecording implements JsonSerializable {

    private static final String ACKNOWLEDGE_AUDIO_ITEMS_PROPERTY = "acknowledgeAudioItems";
    private static final String RECORDING_CONFIGURATION_PROPERTY = "recordingConfiguration";
    private static final String NO_INPUT_TIMEOUT_PROPERTY = "noInputTimeout";

    private final RecordingConfiguration mRecordingConfiguration;
    private final TimeValue mNoInputTimeout;
    private final List<AudioItem> mAcknowledgeAudioItems;

    public InteractionRecording(RecordingConfiguration recordingConfiguration,
                                TimeValue noInputTimeout,
                                AudioItem... acknowledgeAudioItems) {
        this(recordingConfiguration, noInputTimeout, asList(acknowledgeAudioItems));
    }

    public InteractionRecording(RecordingConfiguration recordingConfiguration,
                                TimeValue noInputTimeout,
                                List<? extends AudioItem> acknowledgeAudioItems) {
        Assert.notNull(recordingConfiguration, "recordingConfiguration");
        Assert.notNull(acknowledgeAudioItems, "acknowledgeAudioItems");
        mRecordingConfiguration = recordingConfiguration;
        mNoInputTimeout = noInputTimeout;
        mAcknowledgeAudioItems = new ArrayList<AudioItem>(acknowledgeAudioItems);
    }

    public RecordingConfiguration getRecordingConfiguration() {
        return mRecordingConfiguration;
    }

    public TimeValue getNoInputTimeout() {
        return mNoInputTimeout;
    }

    public List<? extends AudioItem> getAcknowledgeAudioItems() {
        return mAcknowledgeAudioItems;
    }

    @Override
    public JsonValue asJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.addTimeProperty(builder, NO_INPUT_TIMEOUT_PROPERTY, mNoInputTimeout);
        JsonUtils.add(builder, RECORDING_CONFIGURATION_PROPERTY, mRecordingConfiguration);
        JsonUtils.add(builder, ACKNOWLEDGE_AUDIO_ITEMS_PROPERTY, JsonUtils.toJson(mAcknowledgeAudioItems));
        return builder.build();
    }

    @Override
    public String toString() {
        return asJson().toString();
    }
}