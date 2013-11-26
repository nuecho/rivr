/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.rendering.voicexml.errorhandling;

import org.w3c.dom.*;

import com.nuecho.rivr.voicexml.servlet.*;
import com.nuecho.rivr.voicexml.turn.*;

/**
 * Generates part of the VoiceXML document responsible for handling of VoiceXML
 * fatal errors (error events that can't be handle by the HTTP server).
 * <p>
 * Normally, VoiceXML <code>error</code> events are sent back to the dialogue
 * for processing. However, if another error occurs when the first error is sent
 * back to the server, we have a case of fatal error and must have an error
 * handler in the VoiceXML to deal with these errors. This class contains the
 * {@link #addFatalErrorForm(Element, VoiceXmlDocumentTurn)} method which is
 * invoked to add the proper VoiceXML elements in order to deal properly with
 * such errors.
 * <p>
 * By default, the {@link VoiceXmlDialogueServlet} is initialized with a
 * {@link ExitFatalErrorFormFactory}, which generates an empty
 * <code>&lt;exit&gt;</code> element. This is generally acceptable. However,
 * some contexts would require a different handler, such as subdialogues where a
 * <code>&lt;return&gt;</code> would be more appropriate.
 * 
 * @see ExitFatalErrorFormFactory
 * @see ReturnFatalErrorFormFactory
 * @author Nu Echo Inc.
 */
public interface FatalErrorFormFactory {
    void addFatalErrorForm(Element form, VoiceXmlDocumentTurn turn);
}
