/*
 * Copyright (c) 2012 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.core.util;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.w3c.dom.bootstrap.*;
import org.w3c.dom.ls.*;

/**
 * @author Nu Echo Inc.
 */
public final class DomUtils {

    private static final String FORMAT_PRETTY_PRINT_DOM_CONFIG_PARAMETER = "format-pretty-print";

    private static final DOMImplementationLS DOM_IMPLEMENTATION;
    private static final DocumentBuilder DOCUMENT_BUILDER;

    static {
        try {
            DOCUMENT_BUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException exception) {
            throw new AssertionError(exception);
        }

        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOM_IMPLEMENTATION = (DOMImplementationLS) registry.getDOMImplementation("LS");
        } catch (Exception exception) {
            throw new AssertionError(exception);
        }
    }

    private DomUtils() {
        //utility class: instantiation forbidden
    }

    public static Document createDocument(String rootElementTagName) {
        Document document = createDocument();
        Element rootElement = document.createElement(rootElementTagName);
        document.appendChild(rootElement);
        return document;
    }

    public static Document createDocument() {
        return DOCUMENT_BUILDER.newDocument();
    }

    public static Element appendNewElement(Node parent, String elementName) {
        Element elementNode = parent.getOwnerDocument().createElement(elementName);
        parent.appendChild(elementNode);
        return elementNode;
    }

    public static void appendNewComment(Element parentElement, String commentText) {
        Comment commentNode = parentElement.getOwnerDocument().createComment(commentText);
        parentElement.appendChild(commentNode);
    }

    public static void appendNewText(Element parentElement, String text) {
        Text textNode = parentElement.getOwnerDocument().createTextNode(text);
        parentElement.appendChild(textNode);
    }

    public static void appendNewCData(Element parentElement, String text) {
        Text textNode = parentElement.getOwnerDocument().createCDATASection(text);
        parentElement.appendChild(textNode);
    }

    public static void writeToOutputStream(Node node, OutputStream outputStream) throws IOException {

        LSSerializer xmlSerializer = getSerializer();

        LSOutput destination = DOM_IMPLEMENTATION.createLSOutput();
        destination.setByteStream(outputStream);

        try {
            xmlSerializer.write(node, destination);
        } catch (LSException exception) {
            throw new IOException("Error while writing document to output stream.", exception);
        }
    }

    public static String writeToString(Node node) throws IOException {

        LSSerializer xmlSerializer = getSerializer();

        StringWriter stringWriter = new StringWriter();

        LSOutput destination = DOM_IMPLEMENTATION.createLSOutput();
        destination.setCharacterStream(stringWriter);

        try {
            xmlSerializer.write(node, destination);
        } catch (LSException exception) {
            throw new IOException("Error while writing document to output stream.", exception);
        }

        return stringWriter.toString();
    }

    private static LSSerializer getSerializer() {
        LSSerializer xmlSerializer = DOM_IMPLEMENTATION.createLSSerializer();

        if (xmlSerializer.getDomConfig().canSetParameter(FORMAT_PRETTY_PRINT_DOM_CONFIG_PARAMETER, Boolean.TRUE)) {
            xmlSerializer.getDomConfig().setParameter(FORMAT_PRETTY_PRINT_DOM_CONFIG_PARAMETER, Boolean.TRUE);
        }
        return xmlSerializer;
    }

}