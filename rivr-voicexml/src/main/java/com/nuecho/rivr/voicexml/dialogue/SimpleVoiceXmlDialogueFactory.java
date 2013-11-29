/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.dialogue;

import java.lang.reflect.*;

import com.nuecho.rivr.core.dialogue.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.servlet.*;
import com.nuecho.rivr.voicexml.turn.first.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.turn.last.*;
import com.nuecho.rivr.voicexml.turn.output.*;

/**
 * Dialogue factory creating instances of a given {@link VoiceXmlDialogue}
 * class. This is the {@link VoiceXmlDialogueFactory} used by the
 * {@link VoiceXmlDialogueServlet} when the
 * <code>com.nuecho.rivr.voicexml.dialogue.class</code> servlet init-arg is
 * specified in {@link VoiceXmlDialogueServlet}.
 * <p>
 * This factory can also provide the same instance of a give
 * {@link VoiceXmlDialogue} when the
 * {@link SimpleVoiceXmlDialogueFactory#SimpleVoiceXmlDialogueFactory(VoiceXmlDialogue)}
 * constructor is used.
 * 
 * @see VoiceXmlDialogueServlet
 * @author Nu Echo Inc.
 */
public class SimpleVoiceXmlDialogueFactory implements VoiceXmlDialogueFactory {

    private Class<? extends VoiceXmlDialogue> mVoiceXmlDialogueClass;
    private VoiceXmlDialogue mDialogue;

    public SimpleVoiceXmlDialogueFactory(VoiceXmlDialogue dialogue) {
        Assert.notNull(dialogue, "dialogue");
        mDialogue = dialogue;
    }

    public SimpleVoiceXmlDialogueFactory(Class<? extends VoiceXmlDialogue> voiceXmlDialogueClass)
            throws DialogueFactoryException {
        Assert.notNull(voiceXmlDialogueClass, "voiceXmlDialogueClass");
        mVoiceXmlDialogueClass = voiceXmlDialogueClass;
        if (mVoiceXmlDialogueClass.isInterface())
            throw new DialogueFactoryException("Dialogue cannot be an interface.");

        int modifiers = mVoiceXmlDialogueClass.getModifiers();
        if (Modifier.isAbstract(modifiers))
            throw new DialogueFactoryException("Dialogue class should not be abstract.");

        if (!Modifier.isPublic(modifiers)) throw new DialogueFactoryException("Dialogue class should be public.");

        try {
            Constructor<? extends VoiceXmlDialogue> constructor = mVoiceXmlDialogueClass.getConstructor(new Class[0]);

            int constructorModifier = constructor.getModifiers();

            if (!Modifier.isPublic(constructorModifier))
                throw new DialogueFactoryException("Dialogue class nullary constructor should be public.");

        } catch (SecurityException exception) {
            throw new DialogueFactoryException("Unable to access dialogue class nullary constructor.", exception);
        } catch (NoSuchMethodException exception) {
            throw new DialogueFactoryException("Missing public nullary constructor in dialogue class.", exception);
        }
    }

    @Override
    public Dialogue<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlFirstTurn, VoiceXmlLastTurn, VoiceXmlDialogueContext> create(DialogueInitializationInfo<VoiceXmlInputTurn, VoiceXmlOutputTurn, VoiceXmlDialogueContext> dialogueInitializationInfo)
            throws DialogueFactoryException {

        if (mDialogue != null) return mDialogue;

        try {
            return mVoiceXmlDialogueClass.newInstance();
        } catch (InstantiationException exception) {
            throw new DialogueFactoryException("Problem creating instance of dialogue.", exception);
        } catch (IllegalAccessException exception) {
            throw new DialogueFactoryException("Problem creating instance of dialogue.", exception);
        }
    }

}
