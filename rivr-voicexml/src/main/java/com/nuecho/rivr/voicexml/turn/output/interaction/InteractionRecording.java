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
 * An <code>InteractionRecording</code> is an optional final phase of an
 * {@link Interaction}.
 * <p>
 * It specifies a recording configuration, and optionally, a no input timeout
 * and a sequence of {@link AudioItem} that is played if a recording is
 * successful.
 * 
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mAcknowledgeAudioItems == null) ? 0 : mAcknowledgeAudioItems.hashCode());
        result = prime * result + ((mNoInputTimeout == null) ? 0 : mNoInputTimeout.hashCode());
        result = prime * result + ((mRecordingConfiguration == null) ? 0 : mRecordingConfiguration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        InteractionRecording other = (InteractionRecording) obj;
        if (mAcknowledgeAudioItems == null) {
            if (other.mAcknowledgeAudioItems != null) return false;
        } else if (!mAcknowledgeAudioItems.equals(other.mAcknowledgeAudioItems)) return false;
        if (mNoInputTimeout == null) {
            if (other.mNoInputTimeout != null) return false;
        } else if (!mNoInputTimeout.equals(other.mNoInputTimeout)) return false;
        if (mRecordingConfiguration == null) {
            if (other.mRecordingConfiguration != null) return false;
        } else if (!mRecordingConfiguration.equals(other.mRecordingConfiguration)) return false;
        return true;
    }

    @Override
    public String toString() {
        return asJson().toString();
    }
}