/*
 * Copyright (c) 2004 Nu Echo Inc. All rights reserved.
 */
package com.nuecho.rivr.voicexml.rendering.voicexml;

import java.text.*;
import java.util.*;
import java.util.regex.*;

import org.w3c.dom.*;

import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.servlet.*;
import com.nuecho.rivr.voicexml.turn.*;
import com.nuecho.rivr.voicexml.turn.output.*;
import com.nuecho.rivr.voicexml.turn.output.audio.*;
import com.nuecho.rivr.voicexml.turn.output.fetch.*;
import com.nuecho.rivr.voicexml.turn.output.grammar.*;
import com.nuecho.rivr.voicexml.turn.output.interaction.*;

/**
 * @author Nu Echo Inc.
 */
public final class VoiceXmlDomUtil {

    public static final String PARAM_ELEMENT = "param";
    public static final String ASSIGN_ELEMENT = "assign";
    public static final String VXML_ELEMENT = "vxml";
    public static final String EXIT_ELEMENT = "exit";
    public static final String BLOCK_ELEMENT = "block";
    public static final String IF_ELEMENT = "if";
    public static final String ELSE_ELEMENT = "else";
    public static final String FORM_ELEMENT = "form";
    public static final String FIELD_ELEMENT = "field";
    public static final String RECORD_ELEMENT = "record";
    public static final String FILLED_ELEMENT = "filled";
    public static final String GRAMMAR_ELEMENT = "grammar";
    public static final String LOG_ELEMENT = "log";
    public static final String SCRIPT_ELEMENT = "script";
    public static final String NOINPUT_ELEMENT = "noinput";
    public static final String NOMATCH_ELEMENT = "nomatch";
    public static final String RETURN_ELEMENT = "return";
    public static final String DISCONNECT_ELEMENT = "disconnect";
    public static final String CATCH_ELEMENT = "catch";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String SUBMIT_ELEMENT = "submit";
    public static final String VALUE_ELEMENT = "value";
    public static final String VAR_ELEMENT = "var";
    public static final String BREAK_ELEMENT = "break";
    public static final String MARK_ELEMENT = "mark";
    public static final String AUDIO_ELEMENT = "audio";
    public static final String GOTO_ELEMENT = "goto";
    public static final String TRANSFER_ELEMENT = "transfer";
    public static final String SUBDIALOG_ELEMENT = "subdialog";
    public static final String OBJECT_ELEMENT = "object";
    public static final String REPROMPT_ELEMENT = "reprompt";

    public static final String BRIDGE_ATTRIBUTE = "bridge";
    public static final String ID_ATTRIBUTE = "id";
    public static final String MODE_ATTRIBUTE = "mode";
    public static final String SRC_ATTRIBUTE = "src";
    public static final String NAME_LIST_ATTRIBUTE = "namelist";
    public static final String COND_ATTRIBUTE = "cond";
    public static final String EVENT_ATTRIBUTE = "event";
    public static final String COUNT_ATTRIBUTE = "count";
    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String MESSAGE_EXPRESSION_ATTRIBUTE = "messageexpr";
    public static final String METHOD_ATTRIBUTE = "method";
    public static final String NEXT_ATTRIBUTE = "next";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String EXPR_ATTRIBUTE = "expr";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String VALUE_TYPE_ATTRIBUTE = "valuetype";
    public static final String TIME_ATTRIBUTE = "time";
    public static final String PROMPT_ATTRIBUTE = "prompt";
    public static final String XML_LANGUAGE_ATTRIBUTE = "xml:lang";
    public static final String APPLICATION_ATTRIBUTE = "application";
    public static final String XMLNS_ATTRIBUTE = "xmlns";
    public static final String VERSION_ATTRIBUTE = "version";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String MAXTIME_ATTRIBUTE = "maxtime";
    public static final String FINAL_SILENCE_ATTRIBUTE = "finalsilence";
    public static final String DTMFTERM_ATTRIBUTE = "dtmfterm";
    public static final String BEEP_ATTRIBUTE = "beep";
    public static final String ENCTYPE_ATTRIBUTE = "enctype";
    public static final String CONNECT_TIMEOUT_ATTRIBUTE = "connecttimeout";
    public static final String TRANSFER_AUDIO_ATTRIBUTE = "transferaudio";
    public static final String DEST_ATTRIBUTE = "dest";
    public static final String AAI_ATTRIBUTE = "aai";
    public static final String FETCH_AUDIO_ATTRIBUTE = "fetchaudio";
    public static final String FETCH_TIMEOUT_ATTRIBUTE = "fetchtimeout";
    public static final String FETCH_HINT_ATTRIBUTE = "fetchhint";
    public static final String MAX_STALE_ATTRIBUTE = "maxstale";
    public static final String MAX_AGE_ATTRIBUTE = "maxage";
    public static final String TAG_FORMAT_ATTRIBUTE = "tag-format";
    public static final String ROOT_ATTRIBUTE = "root";
    public static final String BASE_ATTRIBUTE = "base";
    public static final String WEIGHT_ATTRIBUTE = "weight";
    public static final String ARCHIVE_ATTRIBUTE = "archive";
    public static final String DATA_ATTRIBUTE = "data";
    public static final String CODE_TYPE_ATTRIBUTE = "codetype";
    public static final String CODE_BASE_ATTRIBUTE = "codebase";
    public static final String CLASS_ID_ATTRIBUTE = "classid";

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static final String POST_METHOD = "post";
    public static final String GET_METHOD = "get";

    public static final String PROMPT_FORM_ITEM_NAME_PREFIX = "prompt";
    public static final String RECOGNITION_FORM_ITEM_NAME = "recognition";
    public static final String SUBDIALOGUE_FORM_ITEM_NAME = "subdialogue";
    public static final String OBJECT_FORM_ITEM_NAME = "object";
    public static final String RECORD_FORM_ITEM_NAME = "record";
    public static final String TRANSFER_FORM_ITEM_NAME = "transfer";

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ANY_MODE = "any";

    public static final String BARGE_IN_PROPERTY = "bargein";
    public static final String CONFIDENCE_LEVEL_PROPERTY = "confidencelevel";
    public static final String MAX_N_BEST_PROPERTY = "maxnbest";
    public static final String INPUT_MODES_PROPERTY = "inputmodes";
    public static final String INCOMPLETE_TIMEOUT_PROPERTY = "incompletetimeout";
    public static final String COMPLETE_TIMEOUT_PROPERTY = "completetimeout";
    public static final String TIMEOUT_PROPERTY = "timeout";
    public static final String BARGE_IN_TYPE_PROPERTY = "bargeintype";
    public static final String SPEED_VERSUS_ACCURACY_PROPERTY = "speedvsaccuracy";
    public static final String SENSITIVITY_PROPERTY = "sensitivity";
    public static final String MAX_SPEECH_TIMEOUT_PROPERTY = "maxspeechtimeout";
    public static final String TERM_CHAR_PROPERTY = "termchar";
    public static final String TERM_TIMEOUT_PROPERTY = "termtimeout";
    public static final String INTER_DIGIT_TIMEOUT_PROPERTY = "interdigittimeout";
    public static final String FETCH_AUDIO_MINIMUM_PROPERTY = "fetchaudiominimum";
    public static final String FETCH_AUDIO_DELAY_PROPERTY = "fetchaudiodelay";
    public static final String FETCH_AUDIO_PROPERTY = "fetchaudio";
    public static final String FETCH_TIMEOUT_PROPERTY = "fetchtimeout";

    public static final String MAX_STALE_PROPERTY_SUFFIX = "maxstale";
    public static final String MAX_AGE_PROPERTY_SUFFIX = "maxage";
    public static final String FETCH_HINT_PROPERTY_SUFFIX = "fetchhint";

    public static final String MILLISECOND_UNIT_SUFFIX = "ms";
    public static final String FORM_ID = "form";
    public static final String PREFETCH_PROPERTY_VALUE = "prefetch";

    public static final String DTMF_VOICE_INPUT_MODE = "dtmf voice";
    public static final String DTMF_INPUT_MODE = "dtmf";
    public static final String VOICE_INPUT_MODE = "voice";

    public static final String ERROR_EVENT_NAME = "error";

    public static final String FATAL_ERROR_HANDLER_FORM_ID = "fatalErrorForm";
    public static final String SUBMIT_FORM_ID = "submitForm";

    public static final String EVENT_MESSAGE_VARIABLE = "_message";
    public static final String EVENT_NAME_VARIABLE = "_event";
    public static final String INPUT_TURN_VARIABLE = "inputTurn";
    public static final String RECORDING_VARIABLE = "recording";

    public static final String RIVR_VARIABLE = "rivr";
    public static final String RIVR_SCOPE_OBJECT = "application." + RIVR_VARIABLE;

    public static final String RIVR_INPUT_TURN_PROPERTY = "inputTurn";
    public static final String RIVR_INPUT_TURN_SCOPE_OBJECT = RIVR_SCOPE_OBJECT + "." + RIVR_INPUT_TURN_PROPERTY;

    public static final String RIVR_DIALOGUE_ID_PROPERTY = "dialogueId";
    public static final String RIVR_DIALOGUE_ID_SCOPE_OBJECT = RIVR_SCOPE_OBJECT + "." + RIVR_DIALOGUE_ID_PROPERTY;

    private static final String RIVR_TURN_INDEX_PROPERTY = "turnIndex";
    private static final String RIVR_TURN_NAME_PROPERTY = "turnName";

    public static final String LOCAL_ERROR_HANDLING_PROPERTY = "localErrorHandling";
    public static final String RESULT_RECORDING_METADATA_SCOPE_OBJECT = RIVR_INPUT_TURN_SCOPE_OBJECT
                                                                        + ".recordingMetaData";
    public static final String RESULT_RECORDING_METADATA_DATA_SCOPE_OBJECT = RESULT_RECORDING_METADATA_SCOPE_OBJECT
                                                                             + ".data";

    public static final String VOICEXML_NAMESPACE = "http://www.w3.org/2001/vxml";

    private static final Pattern ECMASCRIPT_BACKSLASH_PATTERN = Pattern.compile("\\\\");
    private static final Pattern ECMASCRIPT_QUOTE_PATTERN = Pattern.compile("\\'");
    private static final Pattern ECMASCRIPT_CARRIAGE_RETURN_PATTERN = Pattern.compile("\\u000d");
    private static final Pattern ECMASCRIPT_LINEFEED_PATTERN = Pattern.compile("\\u000a");
    private static final Pattern ECMASCRIPT_LINE_SEPARATOR_PATTERN = Pattern.compile("\\u2028");
    private static final Pattern ECMASCRIPT_PARAGRAPH_SEPARATOR_PATTERN = Pattern.compile("\\u2029");
    private static final Pattern URI_PATH_REPLACEMENT_CHAR = Pattern.compile("[^a-zA-Z0-9./_-]");

    public static Element createVoiceXmlDocumentRoot(VoiceXmlDialogueContext voiceXmlDialogueContext) {
        Document document = DomUtils.createDocument(VXML_ELEMENT);

        Element vxmlElement = document.getDocumentElement();
        vxmlElement.setAttribute(XMLNS_ATTRIBUTE, VOICEXML_NAMESPACE);
        vxmlElement.setAttribute(VERSION_ATTRIBUTE, "2.1");

        String language = voiceXmlDialogueContext.getLanguage();
        if (language != null) {
            vxmlElement.setAttribute(XML_LANGUAGE_ATTRIBUTE, language);
        }

        return vxmlElement;
    }

    public static Document createDocument(VoiceXmlDialogueContext voiceXmlDialogueContext, NamedTurn turn) {
        Element vxmlElement = createVoiceXmlDocumentRoot(voiceXmlDialogueContext);

        String rootDocumentPath = voiceXmlDialogueContext.getContextPath()
                                  + voiceXmlDialogueContext.getServletPath()
                                  + VoiceXmlDialogueServlet.ROOT_PATH
                                  + voiceXmlDialogueContext.getDialogueId();

        vxmlElement.setAttribute(APPLICATION_ATTRIBUTE, rootDocumentPath);

        StringBuilder script = new StringBuilder();
        script.append(RIVR_SCOPE_OBJECT);
        script.append(".");
        script.append(LOCAL_ERROR_HANDLING_PROPERTY);
        script.append(" = ");
        script.append(FALSE);
        script.append("; ");

        if (turn != null) {
            script.append(RIVR_SCOPE_OBJECT);
            script.append(".");
            script.append(RIVR_TURN_NAME_PROPERTY);
            script.append(" = '");
            script.append(turn.getName());
            script.append("'; ");
        }

        script.append(RIVR_SCOPE_OBJECT);
        script.append(".");
        script.append(RIVR_TURN_INDEX_PROPERTY);
        script.append(" = ");
        script.append(voiceXmlDialogueContext.getTurnIndex());
        script.append("; ");

        script.append(RIVR_INPUT_TURN_SCOPE_OBJECT);
        script.append(" = {};");
        createScript(vxmlElement, script.toString());

        processFetchRendering(voiceXmlDialogueContext, vxmlElement);

        processProperties(voiceXmlDialogueContext, vxmlElement);
        addEventHandlers(vxmlElement);

        return vxmlElement.getOwnerDocument();
    }

    private static void processProperties(VoiceXmlDialogueContext voiceXmlDialogueContext, Element vxmlElement) {
        Map<String, String> properties = voiceXmlDialogueContext.getProperties();
        if (properties != null) {
            for (String propertyName : properties.keySet()) {
                addProperty(vxmlElement, propertyName, properties.get(propertyName));
            }
        }
    }

    private static void processFetchRendering(VoiceXmlDialogueContext voiceXmlDialogueContext, Element vxmlElement) {
        FetchConfiguration fetchConfiguration = voiceXmlDialogueContext.getFetchConfiguration();
        if (fetchConfiguration == null) return;

        Recording fetchAudio = fetchConfiguration.getDefaultFetchAudio();
        if (fetchAudio != null) {
            addFetchAudioProperty(vxmlElement, fetchAudio);
        }

        addTimeProperty(vxmlElement, FETCH_TIMEOUT_PROPERTY, fetchConfiguration.getDefaultFetchTimeout());

        applyDefaultResourceFetchConfiguration(vxmlElement, fetchConfiguration.getDefaultAudioFetchConfiguration());
        applyDefaultResourceFetchConfiguration(vxmlElement, fetchConfiguration.getDefaultGrammarFetchConfiguration());
        applyDefaultResourceFetchConfiguration(vxmlElement, fetchConfiguration.getDefaultObjectFetchConfiguration());
        applyDefaultResourceFetchConfiguration(vxmlElement, fetchConfiguration.getDefaultScriptFetchConfiguration());
    }

    public static void createAssignation(Element parent, String variableName, String expression) {
        Element assingElement = DomUtils.appendNewElement(parent, ASSIGN_ELEMENT);
        assingElement.setAttribute(NAME_ATTRIBUTE, variableName);
        assingElement.setAttribute(EXPR_ATTRIBUTE, expression);
    }

    public static void applyRessourceFetchConfiguration(Element element,
                                                        ResourceFetchConfiguration resourceFetchConfiguration) {
        if (resourceFetchConfiguration != null) {
            setCacheControlTimeAttribute(element, MAX_AGE_ATTRIBUTE, resourceFetchConfiguration.getMaxAge());
            setCacheControlTimeAttribute(element, MAX_STALE_ATTRIBUTE, resourceFetchConfiguration.getMaxStale());
            setFetchHintAttribute(element, FETCH_HINT_ATTRIBUTE, resourceFetchConfiguration.getFetchHint());
            setTimeAttribute(element, FETCH_TIMEOUT_ATTRIBUTE, resourceFetchConfiguration.getTimeOut());
        }
    }

    private static void applyDefaultResourceFetchConfiguration(Element parent,
                                                               BaseResourceFetchConfiguration fetchConfiguration) {
        if (fetchConfiguration != null) {
            String name = fetchConfiguration.getName();
            addFetchHintProperty(parent, name + FETCH_HINT_PROPERTY_SUFFIX, fetchConfiguration.getFetchHint());
            addCacheControlTimeProperty(parent, name + MAX_AGE_PROPERTY_SUFFIX, fetchConfiguration.getMaxAge());
            addCacheControlTimeProperty(parent, name + MAX_STALE_PROPERTY_SUFFIX, fetchConfiguration.getMaxStale());
        }
    }

    public static void createPrompt(String language,
                                    Element parent,
                                    VoiceXmlDialogueContext voiceXmlDialogueContext,
                                    Boolean bargeIn,
                                    List<? extends AudioItem> audioItems) throws VoiceXmlDocumentRenderingException {

        Element promptElement = DomUtils.appendNewElement(parent, PROMPT_ATTRIBUTE);
        setBooleanAttribute(promptElement, BARGE_IN_PROPERTY, bargeIn);

        String documentLanguage = voiceXmlDialogueContext.getLanguage();
        if (language != null && !language.equals(documentLanguage)) {
            promptElement.setAttribute(XML_LANGUAGE_ATTRIBUTE, language);
        }

        processAudioItems(audioItems, promptElement);
    }

    public static void processAudioItems(List<? extends AudioItem> audioItems, Element promptElement)
            throws VoiceXmlDocumentRenderingException {

        boolean lastItemWasText = false;
        for (AudioItem audioItem : audioItems) {
            if (audioItem instanceof Recording) {
                Recording recording = (Recording) audioItem;
                Element audioElement = DomUtils.appendNewElement(promptElement, AUDIO_ELEMENT);
                audioElement.setAttribute(SRC_ATTRIBUTE, recording.getPath());

                SynthesisText synthesis = recording.getSynthesisText();

                if (synthesis != null) {
                    if (synthesis.isSsml()) {
                        DocumentFragment ssmlNodes = synthesis.getDocumentFragment();
                        audioElement.appendChild(audioElement.getOwnerDocument().importNode(ssmlNodes, true));
                        lastItemWasText = false;
                    } else {
                        DomUtils.appendNewText(audioElement, synthesis.getText());
                        lastItemWasText = true;
                    }
                }

                applyRessourceFetchConfiguration(audioElement, recording.getResourceFetchConfiguration());
            } else if (audioItem instanceof SynthesisText) {
                SynthesisText synthesisText = (SynthesisText) audioItem;

                if (synthesisText.isSsml()) {
                    NodeList childNodes = synthesisText.getDocumentFragment().getChildNodes();
                    Document document = promptElement.getOwnerDocument();

                    for (int childNodeIndex = 0; childNodeIndex < childNodes.getLength(); childNodeIndex++) {
                        Node node = document.importNode(childNodes.item(childNodeIndex).cloneNode(true), true);
                        promptElement.appendChild(node);
                    }
                    lastItemWasText = false;
                } else {
                    String spaceIfRequired = lastItemWasText ? " " : "";
                    DomUtils.appendNewText(promptElement, spaceIfRequired + synthesisText.getText());
                    lastItemWasText = true;
                }
            } else if (audioItem instanceof Pause) {
                Pause pause = (Pause) audioItem;
                Element breakElement = DomUtils.appendNewElement(promptElement, BREAK_ELEMENT);
                setTimeAttribute(breakElement, TIME_ATTRIBUTE, pause.getDuration());
                lastItemWasText = false;
            } else if (audioItem instanceof Mark) {
                Mark mark = (Mark) audioItem;
                Element markElement = DomUtils.appendNewElement(promptElement, MARK_ELEMENT);
                markElement.setAttribute(NAME_ATTRIBUTE, mark.getName());
                lastItemWasText = false;
            } else if (audioItem instanceof ClientSideRecording) {
                ClientSideRecording clientSideRecording = (ClientSideRecording) audioItem;
                Element audioElement = DomUtils.appendNewElement(promptElement, AUDIO_ELEMENT);
                audioElement.setAttribute(EXPR_ATTRIBUTE, clientSideRecording.getExpression());
                lastItemWasText = false;
            } else throw new VoiceXmlDocumentRenderingException("Cannot handle prompt rendering element of type '"
                                                                + audioItem.getClass()
                                                                + "'");
        }
    }

    public static void addFetchHintProperty(Element parent, String propertyName, FetchHint fetchHint) {
        if (fetchHint != null) {
            addProperty(parent, propertyName, fetchHint.getKey());
        }
    }

    public static void setFetchHintAttribute(Element parent, String propertyName, FetchHint fetchHint) {
        if (fetchHint != null) {
            setAttribute(parent, propertyName, fetchHint.getKey());
        }
    }

    public static void addCacheControlTimeProperty(Element parent, String propertyName, TimeValue value) {
        if (value != null) {
            addProperty(parent, propertyName, String.valueOf(value.getMilliseconds() / 1000));
        }
    }

    public static void setCacheControlTimeAttribute(Element parent, String propertyName, TimeValue value) {
        if (value != null) {
            setAttribute(parent, propertyName, String.valueOf(value.getMilliseconds() / 1000));
        }
    }

    public static void addTimeProperty(Element parent, String propertyName, TimeValue value) {
        if (value != null) {
            addProperty(parent, propertyName, value.getMilliseconds() + MILLISECOND_UNIT_SUFFIX);
        }
    }

    public static void setTimeAttribute(Element element, String attributeName, TimeValue value) {
        if (value != null) {
            setAttribute(element, attributeName, value.getMilliseconds() + MILLISECOND_UNIT_SUFFIX);
        }
    }

    public static void addBooleanProperty(Element parent, String propertyName, Boolean value) {
        if (value != null) {
            addProperty(parent, propertyName, value.booleanValue() ? TRUE : FALSE);
        }
    }

    public static void setBooleanAttribute(Element element, String attributeName, Boolean value) {
        if (value != null) {
            setAttribute(element, attributeName, value.booleanValue() ? TRUE : FALSE);
        }
    }

    public static void setAttribute(Element element, String attributeName, String value) {
        if (value != null) {
            element.setAttribute(attributeName, value);
        }
    }

    public static void addProperty(Element parent, String propertyName, String propertyValue) {
        Assert.notNull(propertyName, "propertyName");

        if (propertyValue != null) {
            Element propertyElement = DomUtils.appendNewElement(parent, PROPERTY_ELEMENT);
            propertyElement.setAttribute(NAME_ATTRIBUTE, propertyName);
            propertyElement.setAttribute(VALUE_ELEMENT, propertyValue);
        }
    }

    public static void addFetchAudioProperty(Element parent, Recording fetchAudioRecording) {
        addProperty(parent, FETCH_AUDIO_PROPERTY, fetchAudioRecording.getPath());
    }

    public static void addVariableDeclarations(Element formElement, VariableDeclarationList variableList) {
        for (VariableDeclaration declaration : variableList) {
            createVarElement(formElement, declaration.getName(), declaration.getInitialValueExpression());
        }
    }

    public static String createEcmaScriptStringLiteral(String content) {
        if (content == null) return "undefined";

        //believe it: this creates \\ for each \
        content = ECMASCRIPT_BACKSLASH_PATTERN.matcher(content).replaceAll("\\\\\\\\");

        content = ECMASCRIPT_QUOTE_PATTERN.matcher(content).replaceAll("\\\\'");
        content = ECMASCRIPT_LINEFEED_PATTERN.matcher(content).replaceAll("\\u000a");
        content = ECMASCRIPT_CARRIAGE_RETURN_PATTERN.matcher(content).replaceAll("\\u000d");
        content = ECMASCRIPT_LINE_SEPARATOR_PATTERN.matcher(content).replaceAll("\\u2028");
        content = ECMASCRIPT_PARAGRAPH_SEPARATOR_PATTERN.matcher(content).replaceAll("\\u2029");

        StringBuffer out = new StringBuffer();
        out.append("'");
        out.append(content);
        out.append("'");
        return out.toString();
    }

    public static void createVarElement(Element parent, String name, String expr) {
        Element varElement = DomUtils.appendNewElement(parent, VAR_ELEMENT);
        varElement.setAttribute(NAME_ATTRIBUTE, name);

        if (expr != null) {
            varElement.setAttribute(EXPR_ATTRIBUTE, expr);
        }
    }

    private static Element createSubmitElement(Element parent,
                                               VoiceXmlDialogueContext voiceXmlDialogueContext,
                                               SubmitMethod method,
                                               VoiceXmlOutputTurn outputTurn,
                                               String... nameList) {
        Element submitElement = DomUtils.appendNewElement(parent, SUBMIT_ELEMENT);
        submitElement.setAttribute(NEXT_ATTRIBUTE, getSubmitPathForTurn(voiceXmlDialogueContext, outputTurn));

        submitElement.setAttribute(NAME_LIST_ATTRIBUTE, StringUtils.join(nameList, " "));
        submitElement.setAttribute(METHOD_ATTRIBUTE, method.getValue());

        applyDocumentFetchConfiguration(submitElement, voiceXmlDialogueContext);

        return submitElement;
    }

    public static String getSubmitPathForTurn(VoiceXmlDialogueContext voiceXmlDialogueContext,
                                              VoiceXmlOutputTurn outputTurn) {
        return getSubmitPath(voiceXmlDialogueContext, voiceXmlDialogueContext.getTurnIndex(), outputTurn.getName());
    }

    public static String getSubmitPath(VoiceXmlDialogueContext voiceXmlDialogueContext, int turnIndex, String turnName) {
        return getServletPathWithSessionId(voiceXmlDialogueContext)
               + "/"
               + turnIndex
               + "/"
               + StringUtils.replaceAll(URI_PATH_REPLACEMENT_CHAR, turnName, "_");
    }

    public static String getServletPathWithSessionId(VoiceXmlDialogueContext voiceXmlDialogueContext) {
        return voiceXmlDialogueContext.getContextPath()
               + voiceXmlDialogueContext.getServletPath()
               + "/"
               + voiceXmlDialogueContext.getDialogueId();
    }

    public static void applyDocumentFetchConfiguration(Element submitElement,
                                                       VoiceXmlDialogueContext voiceXmlDialogueContext) {
        FetchConfiguration fetchConfiguration = voiceXmlDialogueContext.getFetchConfiguration();
        if (fetchConfiguration == null) return;

        DocumentFetchConfiguration documentFetchConfiguration = fetchConfiguration.getDocumentFetchConfiguration();

        applyDocumentFetchConfiguration(submitElement, documentFetchConfiguration);
    }

    public static void applyDocumentFetchConfiguration(Element submitElement,
                                                       DocumentFetchConfiguration submitTurnFetchConfiguration) {
        if (submitTurnFetchConfiguration == null) return;
        TimeValue timeOut = submitTurnFetchConfiguration.getTimeOut();
        setTimeAttribute(submitElement, FETCH_TIMEOUT_PROPERTY, timeOut);

        Recording fetchAudio = submitTurnFetchConfiguration.getFetchAudio();
        applyFetchAudio(submitElement, fetchAudio);
    }

    public static void applyFetchAudio(Element element, Recording fetchAudio) {
        if (fetchAudio != null) {
            String fetchAudioHref = fetchAudio.getPath();
            Assert.notNull(fetchAudioHref, "fetchAudioHref");
            element.setAttribute(FETCH_AUDIO_ATTRIBUTE, fetchAudioHref);
        }
    }

    public static void addEventHandlers(Element vxmlElement) {
        Element catchElement = DomUtils.appendNewElement(vxmlElement, CATCH_ELEMENT);

        Element ifErrorElement = DomUtils.appendNewElement(catchElement, IF_ELEMENT);
        ifErrorElement.setAttribute(COND_ATTRIBUTE, "_event.substring(0, 5) == \"error\"");

        Element ifErrorHandlingElement = DomUtils.appendNewElement(ifErrorElement, IF_ELEMENT);
        ifErrorHandlingElement.setAttribute(COND_ATTRIBUTE, RIVR_SCOPE_OBJECT + "." + LOCAL_ERROR_HANDLING_PROPERTY);
        createGotoFatalHandler(ifErrorHandlingElement);

        DomUtils.appendNewElement(ifErrorHandlingElement, ELSE_ELEMENT);

        StringBuilder setErrorHandlingScript = new StringBuilder();
        setErrorHandlingScript.append(RIVR_SCOPE_OBJECT);
        setErrorHandlingScript.append(".");
        setErrorHandlingScript.append(LOCAL_ERROR_HANDLING_PROPERTY);
        setErrorHandlingScript.append("=");
        setErrorHandlingScript.append(TRUE);
        createScript(ifErrorHandlingElement, setErrorHandlingScript.toString());

        StringBuilder addEventScript = new StringBuilder();
        addEventScript.append(RIVR_SCOPE_OBJECT);
        addEventScript.append(".addEventResult(");
        addEventScript.append(EVENT_NAME_VARIABLE);
        addEventScript.append(", ");
        addEventScript.append(EVENT_MESSAGE_VARIABLE);
        addEventScript.append(")");
        createScript(catchElement, addEventScript.toString());

        createGotoSubmit(catchElement);
    }

    public static void createGotoSubmit(Element parent) {
        Element gotoElement = DomUtils.appendNewElement(parent, GOTO_ELEMENT);
        gotoElement.setAttribute(NEXT_ATTRIBUTE, "#" + SUBMIT_FORM_ID);
    }

    public static void createGotoFatalHandler(Element parent) {
        Element gotoElement = DomUtils.appendNewElement(parent, GOTO_ELEMENT);
        gotoElement.setAttribute(NEXT_ATTRIBUTE, "#" + FATAL_ERROR_HANDLER_FORM_ID);
    }

    public static void createScript(Element parent, String script) {
        Element scriptElement = DomUtils.appendNewElement(parent, SCRIPT_ELEMENT);
        DomUtils.appendNewText(scriptElement, script);
    }

    public static void addSubmitForm(VoiceXmlDialogueContext dialogueContext, Document document, VoiceXmlOutputTurn turn) {
        Element vxmlElement = document.getDocumentElement();
        Element formElement = DomUtils.appendNewElement(vxmlElement, FORM_ELEMENT);
        formElement.setAttribute(ID_ATTRIBUTE, SUBMIT_FORM_ID);

        Element blockElement = addBlockElement(formElement);
        createVarElement(blockElement, INPUT_TURN_VARIABLE, RIVR_SCOPE_OBJECT
                                                            + ".toJson("
                                                            + RIVR_INPUT_TURN_SCOPE_OBJECT
                                                            + ")");

        Element ifElement = DomUtils.appendNewElement(blockElement, IF_ELEMENT);
        ifElement.setAttribute(COND_ATTRIBUTE, RIVR_SCOPE_OBJECT
                                               + ".hasRecording("
                                               + RIVR_INPUT_TURN_SCOPE_OBJECT
                                               + ")");
        createVarElement(ifElement, RECORDING_VARIABLE, RESULT_RECORDING_METADATA_DATA_SCOPE_OBJECT);
        createAssignation(ifElement, RESULT_RECORDING_METADATA_DATA_SCOPE_OBJECT, "undefined");

        Element submitElement = createSubmitElement(ifElement,
                                                    dialogueContext,
                                                    SubmitMethod.POST,
                                                    turn,
                                                    INPUT_TURN_VARIABLE,
                                                    RECORDING_VARIABLE);
        submitElement.setAttribute(ENCTYPE_ATTRIBUTE, MULTIPART_FORM_DATA);
        DomUtils.appendNewElement(ifElement, ELSE_ELEMENT);

        submitElement = createSubmitElement(ifElement, dialogueContext, SubmitMethod.POST, turn, INPUT_TURN_VARIABLE);
    }

    public static void addFatalErrorHandlerForm(VoiceXmlDialogueContext dialogueContext,
                                                Document document,
                                                VoiceXmlDocumentTurn turn) {
        Element vxmlElement = document.getDocumentElement();
        Element fatalErrorForm = DomUtils.appendNewElement(vxmlElement, FORM_ELEMENT);
        fatalErrorForm.setAttribute(ID_ATTRIBUTE, FATAL_ERROR_HANDLER_FORM_ID);
        dialogueContext.getFatalErrorFormFactory().addFatalErrorForm(fatalErrorForm, turn);
    }

    public static Element addBlockElement(Element formElement) {
        return DomUtils.appendNewElement(formElement, BLOCK_ELEMENT);
    }

    public static void processDtmfConfiguration(DtmfRecognitionConfiguration dtmfRecognitionConfiguration,
                                                Element formItemElement) throws VoiceXmlDocumentRenderingException {
        if (dtmfRecognitionConfiguration == null) return;

        VoiceXmlDomUtil.renderGrammars(dtmfRecognitionConfiguration.getGrammarItems(), formItemElement, DTMF_INPUT_MODE);

        addTimeProperty(formItemElement,
                        INTER_DIGIT_TIMEOUT_PROPERTY,
                        dtmfRecognitionConfiguration.getInterDigitTimeout());

        addTimeProperty(formItemElement, TERM_TIMEOUT_PROPERTY, dtmfRecognitionConfiguration.getTermTimeout());

        addProperty(formItemElement, TERM_CHAR_PROPERTY, dtmfRecognitionConfiguration.getTermChar());

        for (String propertyName : dtmfRecognitionConfiguration.getPropertyNames()) {
            addProperty(formItemElement, propertyName, dtmfRecognitionConfiguration.getProperty(propertyName));
        }
    }

    public static void processSpeechConfiguration(SpeechRecognitionConfiguration speechRecognitionConfiguration,
                                                  Element formItemElement) throws VoiceXmlDocumentRenderingException {
        if (speechRecognitionConfiguration == null) return;

        VoiceXmlDomUtil.renderGrammars(speechRecognitionConfiguration.getGrammarItems(),
                                       formItemElement,
                                       VOICE_INPUT_MODE);

        addTimeProperty(formItemElement, COMPLETE_TIMEOUT_PROPERTY, speechRecognitionConfiguration.getCompleteTimeout());

        addTimeProperty(formItemElement,
                        INCOMPLETE_TIMEOUT_PROPERTY,
                        speechRecognitionConfiguration.getIncompleteTimeout());

        addTimeProperty(formItemElement,
                        MAX_SPEECH_TIMEOUT_PROPERTY,
                        speechRecognitionConfiguration.getMaxSpeechTimeout());

        VoiceXmlDomUtil.addNumberProperty(formItemElement,
                                          MAX_N_BEST_PROPERTY,
                                          speechRecognitionConfiguration.getMaxNBest());

        VoiceXmlDomUtil.addNumberProperty(formItemElement,
                                          CONFIDENCE_LEVEL_PROPERTY,
                                          speechRecognitionConfiguration.getConfidenceLevel());

        VoiceXmlDomUtil.addNumberProperty(formItemElement,
                                          SENSITIVITY_PROPERTY,
                                          speechRecognitionConfiguration.getSensitivity());

        VoiceXmlDomUtil.addNumberProperty(formItemElement,
                                          SPEED_VERSUS_ACCURACY_PROPERTY,
                                          speechRecognitionConfiguration.getSpeedVersusAccuracy());

        for (String propertyName : speechRecognitionConfiguration.getPropertyNames()) {
            addProperty(formItemElement, propertyName, speechRecognitionConfiguration.getProperty(propertyName));
        }
    }

    public static void renderGrammars(List<? extends GrammarItem> grammarItems, Element parent, String mode)
            throws VoiceXmlDocumentRenderingException {
        for (GrammarItem grammarItem : grammarItems) {
            Element grammarElement;

            if (grammarItem instanceof GrammarReference) {
                grammarElement = DomUtils.appendNewElement(parent, GRAMMAR_ELEMENT);
                GrammarReference grammarReference = (GrammarReference) grammarItem;

                VoiceXmlDomUtil.renderGrammarReference(grammarReference, grammarElement);
            } else if (grammarItem instanceof InlineStringGrammar) {
                grammarElement = DomUtils.appendNewElement(parent, GRAMMAR_ELEMENT);
                InlineStringGrammar inlineStringGrammar = (InlineStringGrammar) grammarItem;
                VoiceXmlDomUtil.renderInlineStringGrammar(grammarElement, inlineStringGrammar);
            } else if (grammarItem instanceof InlineXmlGrammar) {
                InlineXmlGrammar inlineXmlGrammar = (InlineXmlGrammar) grammarItem;
                Document document = inlineXmlGrammar.getDocument();
                Element documentRootElement = document.getDocumentElement();
                if (!documentRootElement.getTagName().equals(GRAMMAR_ELEMENT))
                    throw new VoiceXmlDocumentRenderingException("XML grammar root element must be 'gramar'");

                grammarElement = (Element) parent.getOwnerDocument().importNode(documentRootElement, true);
                parent.appendChild(grammarElement);
            } else throw new AssertionError("Unsupported grammar item: " + grammarItem);

            Double weight = grammarItem.getWeight();
            if (weight != null) {
                DecimalFormat weigthDecimalFormat = new DecimalFormat("#.##########");
                double weightDoubleValue = weight.doubleValue();
                grammarElement.setAttribute(WEIGHT_ATTRIBUTE, weigthDecimalFormat.format(weightDoubleValue));
            }

            setAttribute(grammarElement, TYPE_ATTRIBUTE, grammarItem.getMediaType());
            setAttribute(grammarElement, MODE_ATTRIBUTE, mode);
        }
    }

    public static void renderInlineStringGrammar(Element grammarElement, InlineStringGrammar inlineStringGrammar) {
        DomUtils.appendNewCData(grammarElement, inlineStringGrammar.getSource());
        setAttribute(grammarElement, BASE_ATTRIBUTE, inlineStringGrammar.getBase());
        setAttribute(grammarElement, XML_LANGUAGE_ATTRIBUTE, inlineStringGrammar.getLanguage());
        setAttribute(grammarElement, ROOT_ATTRIBUTE, inlineStringGrammar.getRoot());
        setAttribute(grammarElement, TAG_FORMAT_ATTRIBUTE, inlineStringGrammar.getTagFormat());
        setAttribute(grammarElement, VERSION_ATTRIBUTE, inlineStringGrammar.getVersion());
    }

    public static void renderGrammarReference(GrammarReference grammarReference, Element grammarElement) {
        Assert.notNull(grammarReference, "grammarReference");

        grammarElement.setAttribute(SRC_ATTRIBUTE, grammarReference.getUri());

        ResourceFetchConfiguration resourceFetchConfiguration = grammarReference.getResourceFetchConfiguration();
        applyRessourceFetchConfiguration(grammarElement, resourceFetchConfiguration);
    }

    public static void addNumberProperty(Element parent, String propertyName, Number value) {
        if (value != null) {
            addProperty(parent, propertyName, value.toString());
        }
    }

    public static Element createForm(Document document) {
        Element vxmlElement = document.getDocumentElement();
        Element formElement = DomUtils.appendNewElement(vxmlElement, FORM_ELEMENT);
        formElement.setAttribute(ID_ATTRIBUTE, FORM_ID);
        return formElement;
    }

    public static void addNamelist(Element parentElement,
                                   Element namelistHolderElement,
                                   VariableDeclarationList variables) {
        if (variables == null) return;
        if (variables.isEmpty()) return;

        List<String> variableNames = new ArrayList<String>();
        for (VariableDeclaration declaration : variables) {
            variableNames.add(declaration.getName());
            createVarElement(parentElement, declaration.getName(), declaration.getInitialValueExpression());
        }

        namelistHolderElement.setAttribute(NAME_LIST_ATTRIBUTE, StringUtils.join(variableNames, " "));
    }

}
