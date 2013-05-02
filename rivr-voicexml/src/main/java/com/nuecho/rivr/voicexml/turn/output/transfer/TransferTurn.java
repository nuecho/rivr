/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output.transfer;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public abstract class TransferTurn extends VoiceXmlOutputTurn {

    private static final String TRANSFER_TURN_TYPE = "transfer";
    private static final String TRANSFER_TYPE_PROPERTY = "transferType";
    private static final String APPLICATION_TO_APPLICATION_INFORMATION_PROPERTY = "applicationToApplicationInformation";
    private static final String DESTINATION_PROPERTY = "destination";

    private final String mDestination;
    private String mApplicationToApplicationInformation;

    public TransferTurn(String name, String destination) {
        super(name);
        Assert.notEmpty(destination, "destination");
        mDestination = destination;
    }

    public void setApplicationToApplicationInformation(String applicationToApplicationInformation) {
        mApplicationToApplicationInformation = applicationToApplicationInformation;
    }

    public String getDestination() {
        return mDestination;
    }

    public String getApplicationToApplicationInformation() {
        return mApplicationToApplicationInformation;
    }

    @Override
    protected final String getOuputTurnType() {
        return TRANSFER_TURN_TYPE;
    }

    public abstract String getTransferType();

    @Override
    protected JsonValue getTurnAsJson() {
        JsonObjectBuilder builder = JsonUtils.createObjectBuilder();
        JsonUtils.add(builder, DESTINATION_PROPERTY, mDestination);
        JsonUtils.add(builder, APPLICATION_TO_APPLICATION_INFORMATION_PROPERTY, mApplicationToApplicationInformation);
        JsonUtils.add(builder, TRANSFER_TYPE_PROPERTY, getTransferType());
        addJsonProperties(builder);
        return builder.build();
    }

    protected void addJsonProperties(@SuppressWarnings("unused") JsonObjectBuilder builder) {}

    @Override
    public Document createVoiceXmlDocument(VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {
        Document document = createDocument(dialogueContext, null);
        Element formElement = createForm(document);

        Element transferElement = DomUtils.appendNewElement(formElement, TRANSFER_ELEMENT);

        transferElement.setAttribute(TYPE_ATTRIBUTE, getTransferType());
        transferElement.setAttribute(NAME_ATTRIBUTE, TRANSFER_FORM_ITEM_NAME);
        transferElement.setAttribute(DEST_ATTRIBUTE, mDestination);

        customizeTransferElement(transferElement);

        Element filledElement = DomUtils.appendNewElement(transferElement, FILLED_ELEMENT);
        String script = RIVR_SCOPE_OBJECT
                        + ".addTransferResult("
                        + TRANSFER_FORM_ITEM_NAME
                        + ", "
                        + TRANSFER_FORM_ITEM_NAME
                        + "$);";
        createScript(filledElement, script);
        createGotoSubmit(filledElement);

        addSubmitForm(dialogueContext, document, this);

        return document;
    }

    /**
     * @param transferElement
     * @throws VoiceXmlDocumentRenderingException
     */
    protected void customizeTransferElement(Element transferElement) throws VoiceXmlDocumentRenderingException {}

}