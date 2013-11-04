package com.nuecho.rivr.voicexml.dialogue;

import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.voicexml.turn.first.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * VoiceXML implementation of a dialogue factory.
 * 
 * @author Nu Echo Inc.
 */
public interface VoiceXmlDialogueFactory
        extends
        DialogueFactory<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> {}
