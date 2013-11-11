/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.servlet;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import javax.json.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;

import com.nuecho.rivr.core.servlet.*;
import com.nuecho.rivr.core.util.*;
import com.nuecho.rivr.voicexml.turn.first.*;
import com.nuecho.rivr.voicexml.turn.input.*;
import com.nuecho.rivr.voicexml.util.*;
import com.nuecho.rivr.voicexml.util.json.*;

/**
 * @author Nu Echo Inc.
 */
public final class VoiceXmlInputTurnFactory implements
        com.nuecho.rivr.core.servlet.InputTurnFactory<VoiceXmlInputTurn, VoiceXmlFirstTurn> {
    public static final String INPUT_TURN_PARAMETER = "inputTurn";
    public static final String ROOT_PATH = "/root/";

    //general

    //recording-related
    public static final String RECORDING_PARAMETER = "recording";
    public static final String RECORDING_META_DATA_PROPERTY = "recordingMetaData";
    public static final String TERM_CHAR_PROPERTY = "termChar";
    public static final String MAX_TIME_PROPERTY = "maxTime";
    public static final String DURATION_PROPERTY = "duration";

    //event-related
    public static final String EVENTS_PROPERTY = "events";
    public static final String EVENT_NAME_PROPERTY = "name";
    public static final String EVENT_MESSAGE_PROPERTY = "message";

    //recognition-related
    public static final String DTMF_INPUTMODE_VALUE = "dtmf";
    public static final String VOICE_INPUTMODE_VALUE = "voice";
    public static final String INPUT_MODE_PROPERTY = "inputMode";
    public static final String MARK_PROPERTY = "mark";
    public static final String MARK_NAME_PROPERTY = "name";
    public static final String MARK_TIME_PROPERTY = "time";
    public static final String RECOGNITION_PROPERTY = "recognition";
    public static final String RESULT_PROPERTY = "result";

    //transfer-related
    public static final String TRANSFER_PROPERTY = "transfer";
    public static final String TRANSFER_DURATION_PROPERTY = "duration";
    public static final String TRANSFER_STATUS_PROPERTY = "status";

    //for subdialogue, script and object
    public static final String VALUE_PROPERTY = "value";

    private static final Pattern CHAR_SET_PATTERN = Pattern.compile("charset\\s*=\\s*([^ ;])");

    @Override
    public VoiceXmlFirstTurn createFirstTurn(HttpServletRequest request, HttpServletResponse response)
            throws InputTurnFactoryException {

        Map<String, String> parameters = new HashMap<String, String>();

        @SuppressWarnings("unchecked")
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parameters.put(parameterName, request.getParameter(parameterName));
        }

        return new VoiceXmlFirstTurn(parameters);
    }

    @Override
    public VoiceXmlInputTurn createInputTurn(HttpServletRequest request, HttpServletResponse response)
            throws InputTurnFactoryException {

        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, FileUpload> files = new HashMap<String, FileUpload>();
        processRequestParametersAndFiles(request, parameters, files);

        String result = parameters.get(INPUT_TURN_PARAMETER);

        if (result == null)
            throw new InputTurnFactoryException("Unable to process request. Missing '"
                                                + INPUT_TURN_PARAMETER
                                                + "' parameter.");

        JsonReader jsonReader = JsonUtils.createReader(result);
        JsonObject resultObject = jsonReader.readObject();
        VoiceXmlInputTurn voiceXmlInputTurn = new VoiceXmlInputTurn();
        voiceXmlInputTurn.setFiles(files);

        addEvents(resultObject, voiceXmlInputTurn);
        addObject(resultObject, voiceXmlInputTurn);
        addTransferStatusInfo(resultObject, voiceXmlInputTurn);
        addRecognitionInfo(resultObject, voiceXmlInputTurn);
        addRecordingInfo(resultObject, voiceXmlInputTurn, files);
        return voiceXmlInputTurn;
    }

    private static void addEvents(JsonObject resultObject, VoiceXmlInputTurn voiceXmlInputTurn) {
        if (!resultObject.containsKey(EVENTS_PROPERTY)) return;

        List<JsonValue> eventObjects = resultObject.getJsonArray(EVENTS_PROPERTY);
        List<VoiceXmlEvent> events = new ArrayList<VoiceXmlEvent>();
        for (JsonValue jsonValue : eventObjects) {
            JsonObject eventObject = (JsonObject) jsonValue;

            String name = eventObject.getString(EVENT_NAME_PROPERTY);
            String message = eventObject.getString(EVENT_MESSAGE_PROPERTY, null);

            events.add(new VoiceXmlEvent(name, message));
        }

        voiceXmlInputTurn.setEvents(events);
    }

    private static void addObject(JsonObject resultObject, VoiceXmlInputTurn voiceXmlInputTurn) {
        if (!resultObject.containsKey(VALUE_PROPERTY)) return;

        JsonValue subdialogueResultJsonValue = resultObject.get(VALUE_PROPERTY);
        voiceXmlInputTurn.setJsonValue(subdialogueResultJsonValue);
    }

    private static void addTransferStatusInfo(JsonObject resultObject, VoiceXmlInputTurn voiceXmlInputTurn) {
        if (!resultObject.containsKey(TRANSFER_PROPERTY)) return;

        JsonObject transferResultObject = resultObject.getJsonObject(TRANSFER_PROPERTY);

        TransferStatus transferStatus = new TransferStatus(transferResultObject.getString(TRANSFER_STATUS_PROPERTY));

        long durationValue;

        if (transferResultObject.containsKey(TRANSFER_DURATION_PROPERTY)) {
            durationValue = JsonUtils.getLongProperty(transferResultObject, TRANSFER_DURATION_PROPERTY);
        } else {
            durationValue = 0;
        }

        Duration duration = Duration.milliseconds(durationValue);

        voiceXmlInputTurn.setTransferResult(new TransferStatusInfo(transferStatus, duration));
    }

    private static void addRecordingInfo(JsonObject resultObject,
                                         VoiceXmlInputTurn voiceXmlInputTurn,
                                         Map<String, FileUpload> files) {
        if (!resultObject.containsKey(RECORDING_META_DATA_PROPERTY)) return;

        JsonObject recordingMetaData = resultObject.getJsonObject(RECORDING_META_DATA_PROPERTY);

        Duration duration;
        if (recordingMetaData.containsKey(DURATION_PROPERTY)) {
            long durationInMilliseconds = JsonUtils.getLongProperty(recordingMetaData, DURATION_PROPERTY);
            duration = Duration.milliseconds(durationInMilliseconds);
        } else {
            duration = null;
        }

        boolean maxTime = recordingMetaData.getBoolean(MAX_TIME_PROPERTY, false);

        String dtmfTermChar = recordingMetaData.getString(TERM_CHAR_PROPERTY, null);

        FileUpload file;
        if (!files.containsKey(RECORDING_PARAMETER)) {
            file = null;
        } else {
            file = files.get(RECORDING_PARAMETER);
        }

        voiceXmlInputTurn.setRecordingInfo(new RecordingInfo(file, duration, maxTime, dtmfTermChar));
    }

    private void addRecognitionInfo(JsonObject jsonObject, VoiceXmlInputTurn voiceXmlInputTurn) {

        if (!jsonObject.containsKey(RECOGNITION_PROPERTY)) return;

        JsonObject recognitionObject = jsonObject.getJsonObject(RECOGNITION_PROPERTY);

        JsonArray recognitionResultArray = recognitionObject.getJsonArray(RESULT_PROPERTY);

        MarkInfo markInfo = null;
        if (recognitionObject.containsKey(MARK_PROPERTY)) {
            JsonObject markObject = recognitionObject.getJsonObject(MARK_PROPERTY);
            long timeInMilliseconds = JsonUtils.getLongProperty(markObject, MARK_TIME_PROPERTY);
            markInfo = new MarkInfo(markObject.getString(MARK_NAME_PROPERTY), Duration.milliseconds(timeInMilliseconds));
        }

        voiceXmlInputTurn.setRecognitionInfo(new RecognitionInfo(recognitionResultArray, markInfo));
    }

    private void processRequestParametersAndFiles(HttpServletRequest request,
                                                  Map<String, String> parameters,
                                                  Map<String, FileUpload> files) throws InputTurnFactoryException {
        if (ServletFileUpload.isMultipartContent(request)) {
            ServletFileUpload servletFileUpload = new ServletFileUpload();
            try {
                FileItemIterator itemIterator = servletFileUpload.getItemIterator(request);
                while (itemIterator.hasNext()) {
                    FileItemStream fileItemStream = itemIterator.next();

                    byte[] bytes;
                    try {
                        InputStream stream = fileItemStream.openStream();
                        bytes = IOUtils.toByteArray(stream);
                    } catch (IOException exception) {
                        throw new ServletException("Unable to read stream from " + fileItemStream.getFieldName(),
                                                   exception);
                    }

                    if (!fileItemStream.isFormField()) {
                        FileUpload fileUpload = new FileUpload(fileItemStream.getName(),
                                                               fileItemStream.getContentType(),
                                                               bytes,
                                                               getHeaders(fileItemStream));

                        files.put(fileItemStream.getFieldName(), fileUpload);
                    } else {
                        String encoding = findEncoding(request, fileItemStream);
                        parameters.put(fileItemStream.getFieldName(), new String(bytes, encoding));
                    }

                }
            } catch (Exception exception) {
                throw new InputTurnFactoryException("Unable to get recording.", exception);
            }
        } else {
            @SuppressWarnings("unchecked")
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                parameters.put(parameterName, request.getParameter(parameterName));
            }
        }

    }

    private String findEncoding(HttpServletRequest request, FileItemStream fileItemStream) {

        String encoding = null;

        String contentType = fileItemStream.getContentType();

        if (contentType != null && contentType.startsWith("text/plain")) {
            Matcher matcher = CHAR_SET_PATTERN.matcher(contentType);
            if (matcher.find()) {
                encoding = matcher.group(1);
            } else {
                encoding = Encoding.US_ASCII.getId();
            }
        }

        if (encoding == null) {
            encoding = request.getCharacterEncoding();
        }

        if (encoding == null) {
            encoding = Encoding.US_ASCII.getId();
        }

        return encoding;
    }

    private Map<String, String> getHeaders(FileItemStream fileItemStream) {
        Map<String, String> headers = new HashMap<String, String>();
        FileItemHeaders sourceHeaders = fileItemStream.getHeaders();

        if (sourceHeaders == null) return headers;

        @SuppressWarnings("unchecked")
        Iterator<String> headerNames = sourceHeaders.getHeaderNames();
        while (headerNames.hasNext()) {
            String headerName = headerNames.next();
            headers.put(headerName, sourceHeaders.getHeader(headerName));
        }
        return headers;
    }
}