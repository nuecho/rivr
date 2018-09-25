/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.turn.output;

import static com.nuecho.rivr.voicexml.rendering.voicexml.VoiceXmlDomUtil.*;

import javax.json.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.dialogue.*;
import com.nuecho.rivr.voicexml.rendering.voicexml.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * This abstract class is the superclass of all classes representing a transfer
 * to another entity.
 * 
 * @author Nu Echo Inc.
 * @see BlindTransfer
 * @see BridgeTransfer
 * @see ConsultationTransfer
 * @see <a
 *      href="https://www.w3.org/TR/voicexml20/#dml2.3.6">https://www.w3.org/TR/voicexml20/#dml2.3.6</a>
 * @see <a
 *      href="https://www.w3.org/TR/voicexml21/#sec-transfer">https://www.w3.org/TR/voicexml21/#sec-transfer</a>
 */
public abstract class Transfer extends VoiceXmlOutputTurn {
    private static final String TRANSFER_TURN_TYPE = "transfer";

    private static final String TRANSFER_TYPE_PROPERTY = "transferType";
    private static final String APPLICATION_TO_APPLICATION_INFORMATION_PROPERTY = "applicationToApplicationInformation";
    private static final String DESTINATION_PROPERTY = "destination";

    private final String mDestination;
    private String mApplicationToApplicationInformation;

    /**
     * @param name The name of this turn. Not empty.
     * @param destination The URI of the destination (telephone, IP telephony
     *            address). Not empty.
     */
    public Transfer(String name, String destination) {
        super(name);
        Assert.notEmpty(destination, "destination");
        mDestination = destination;
    }

    /**
     * @param applicationToApplicationInformation A string containing data sent
     *            to an application on the far-end, available in the session
     *            variable session.connection.aai.
     */
    public final void setApplicationToApplicationInformation(String applicationToApplicationInformation) {
        mApplicationToApplicationInformation = applicationToApplicationInformation;
    }

    public final String getDestination() {
        return mDestination;
    }

    public final String getApplicationToApplicationInformation() {
        return mApplicationToApplicationInformation;
    }

    protected abstract String getTransferType();

    /**
     * Allows the customization of the generated transfer element
     * 
     * @param transferElement The transfer element to customize.
     * @throws VoiceXmlDocumentRenderingException when an error occurs while
     *             rendering the VoiceXml document
     */
    protected void customizeTransferElement(Element transferElement) throws VoiceXmlDocumentRenderingException {}

    @Override
    protected final String getOuputTurnType() {
        return TRANSFER_TURN_TYPE;
    }

    @Override
    protected void addTurnProperties(JsonObjectBuilder builder) {
        JsonUtils.add(builder, DESTINATION_PROPERTY, mDestination);
        JsonUtils.add(builder, APPLICATION_TO_APPLICATION_INFORMATION_PROPERTY, mApplicationToApplicationInformation);
        JsonUtils.add(builder, TRANSFER_TYPE_PROPERTY, getTransferType());
    }

    @Override
    protected void fillVoiceXmlDocument(Document document, Element formElement, VoiceXmlDialogueContext dialogueContext)
            throws VoiceXmlDocumentRenderingException {

        Element transferElement = DomUtils.appendNewElement(formElement, TRANSFER_ELEMENT);

        transferElement.setAttribute(TYPE_ATTRIBUTE, getTransferType());
        transferElement.setAttribute(NAME_ATTRIBUTE, TRANSFER_FORM_ITEM_NAME);
        transferElement.setAttribute(DEST_ATTRIBUTE, mDestination);

        if (mApplicationToApplicationInformation != null) {
            transferElement.setAttribute(AAI_ATTRIBUTE, mApplicationToApplicationInformation);
        }

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
    }

    /**
     * Base class for all {@link Transfer} builders.
     */
    public abstract static class Builder {

        private final String mName;
        private String mDestination;
        private String mApplicationToApplicationInformation;

        protected Builder(String name) {
            mName = name;
        }

        public Builder setDestination(String destination) {
            mDestination = destination;
            return this;
        }

        public Builder setApplicationToApplication(String applicationToApplicationInformation) {
            mApplicationToApplicationInformation = applicationToApplicationInformation;
            return this;
        }

        protected String getName() {
            return mName;
        }

        protected String getDestination() {
            return mDestination;
        }

        protected void build(Transfer transfer) {
            transfer.setApplicationToApplicationInformation(mApplicationToApplicationInformation);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                 + ((mApplicationToApplicationInformation == null)
                         ? 0
                         : mApplicationToApplicationInformation.hashCode());
        result = prime * result + ((mDestination == null) ? 0 : mDestination.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Transfer other = (Transfer) obj;
        if (mApplicationToApplicationInformation == null) {
            if (other.mApplicationToApplicationInformation != null) return false;
        } else if (!mApplicationToApplicationInformation.equals(other.mApplicationToApplicationInformation))
            return false;
        if (mDestination == null) {
            if (other.mDestination != null) return false;
        } else if (!mDestination.equals(other.mDestination)) return false;
        return true;
    }

}