/*global $, _*/

var configurations;
var configuration;
var servletPath;

var input = {};

function processJson(callback, parameters) {
  parameters = parameters || {};

  $.ajax({
    url : servletPath,
    dataType : 'json',
    data : parameters,
    type : "post",
    success : callback,
    error : function(jqXHR, textStatus, errorThrown) {
      alertDialogue("Error during request: " + textStatus, errorThrown);
    }
  });
}

function addProperties(table, object) {
  if (object === undefined || object === null) {
    return;
  }

  _.each(object, function(propertyValue, propertyName) {
    if (_.isBoolean(propertyValue) || _.isString(propertyValue) || _.isNumber(propertyValue)) {
      addPropertyRow(table, propertyName, propertyValue);
    } else if (_.isObject(propertyValue) && !_.isArray(propertyValue) && !isEmptyRecursive(propertyValue)) {

      addHeader(table, propertyName);
      var newTable = $("<table>").addClass("sub-table");
      table.append($("<tr>").append($("<td>").append(newTable)));
      addProperties(newTable, propertyValue);
    }

  });
}

function isEmptyRecursive(object) {

  var result = true;

  _.each(object, function(value) {
    if (_.isString(value) || _.isNumber(value) || _.isBoolean(value)) {
      return false;
    }

    result = result && isEmptyRecursive(value);
  });

  return result;
}

function addPropertyRow(table, property, value) {
  if (value === null) {
    return;
  }

  var row = $("<tr>");
  table.append(row);
  row.append($("<td>").addClass("content-cell").text(property));
  row.append($("<td>").addClass("content-cell").text(value));
}

function addGrammars(grammars, dtmf, table) {
  _.each(grammars, function(grammar) {
    var grammarRow = $("<tr>");
    table.append(grammarRow);

    var grammarTypeCell = $("<td>");
    grammarTypeCell.addClass("content-cell");

    var type;
    if (dtmf) {
      type = "DTMF";
    } else {
      type = "Speech";
    }

    grammarTypeCell.text(type + " grammar");

    grammarRow.append(grammarTypeCell);

    if (grammar.uri) {
      var grammarUriCell = $("<td>");
      grammarUriCell.addClass("content-cell");
      var link = $("<a>");
      link.attr("href", grammar.uri);
      link.text(grammar.uri);
      grammarUriCell.append(link);
      grammarRow.append(grammarUriCell);
    } else if (grammar.source) {

      var grammarSourceCell = $("<td>");

      var showSourceButton = $("<button>").button({
        "label" : "Show grammar source...",
        "icons" : {
          "primary" : "ui-icon-document"
        }
      }).click(function() {
        $("#longPopupDialogue").dialog("open");
        var grammarSource = $("<pre>");
        grammarSource.text(grammar.source);
        $("#longPopupDialogue").empty();
        $("#longPopupDialogue").append(grammarSource);
      });

      grammarSourceCell.append(showSourceButton);
      grammarSourceCell.addClass("content-cell");
      grammarRow.append(grammarSourceCell);
    }
  });
}

function processConfiguration(configuration, isDtmf, table) {

  if (configuration === undefined || configuration === null) {
    return;
  }

  addGrammars(configuration.grammars, isDtmf, table);
  addProperties(table, _.omit(configuration, "grammars", "properties"));
  addProperties(table, configuration.properties);
}

function processAudioItem(audioItem, table) {
  var promptAudioItemRow = $("<tr>");
  table.append(promptAudioItemRow);

  var audioItemTypeCell = $("<td>");
  audioItemTypeCell.addClass("content-cell");
  promptAudioItemRow.append(audioItemTypeCell);

  var type = audioItem.type;

  if (audioItem.path) {
    var link = $("<a>");
    link.attr("href", audioItem.path);
    link.text(type);
    audioItemTypeCell.append(link);
  } else {
    audioItemTypeCell.text(type);
  }

  var audioItemTextCell = $("<td>");
  audioItemTextCell.addClass("content-cell");
  audioItemTextCell.addClass("prompt-text");
  promptAudioItemRow.append(audioItemTextCell);

  if (audioItem.text) {
    audioItemTextCell.append('"' + audioItem.text + '"');
  }

  if (audioItem.ssml) {
    var showSsmlButton = $("<button>").button({
      "label" : "Show SSML...",
      "icons" : {
        "primary" : "ui-icon-document"
      }
    }).click(function() {
      $("#longPopupDialogue").dialog("open");
      var ssmlPre = $("<pre>");
      ssmlPre.text(audioItem.ssml);
      $("#longPopupDialogue").empty();
      $("#longPopupDialogue").append(ssmlPre);
    });

    audioItemTextCell.append(showSsmlButton);
  }

  if (audioItem.expression) {
    var pre = $("<pre>");
    audioItemTextCell.removeClass("prompt-text");
    pre.text(audioItem.expression);
    audioItemTextCell.append(pre);
  }

  if (audioItem.pause) {
    audioItemTextCell.append(audioItem.pause + " ms");
  }

  if (audioItem.dtmf) {
    audioItemTextCell.append(audioItem.dtmf);
  }

  if (audioItem.mark) {
    audioItemTextCell.append(audioItem.mark);
  }
}

function processPrompt(prompt, table, promptNumber) {
  var hasInput = false;

  addHeader(table, "Prompt #" + promptNumber);

  var dtmfRecognitionConfiguration = prompt.dtmfRecognitionConfiguration;
  if (dtmfRecognitionConfiguration) {
    hasInput = true;
  }

  var speechRecognitionConfiguration = prompt.speechRecognitionConfiguration;
  if (speechRecognitionConfiguration) {
    hasInput = true;
  }

  processConfiguration(dtmfRecognitionConfiguration, true, table);
  processConfiguration(speechRecognitionConfiguration, false, table);

  _.each(prompt.audioItems, function(audioItem) {
    processAudioItem(audioItem, table);
  });

  addProperties(table, _.omit(prompt,
    [ "audioItems", "dtmfRecognitionConfiguration", "speechRecognitionConfiguration" ]));

  return hasInput;
}

function addHeader(table, text) {
  table.append($("<tr>").append($("<td>").addClass("index-cell").text(text).attr("colspan", "2")));
}

function processInteraction(turn) {

  var interaction = turn.data;

  $("#interactionSection").empty();
  var interactionSectionTable = $("<table>");
  $("#interactionSection").append(interactionSectionTable);

  var hasInput = false;
  _.each(interaction.prompts, function(prompt, index) {
    var promptNumber = index + 1;
    hasInput |= processPrompt(prompt, interactionSectionTable, promptNumber);
  });

  var row;

  if (interaction.recognition) {
    var recognition = interaction.recognition;

    row = $("<tr>");
    interactionSectionTable.append(row);

    row.append($("<td>").addClass("index-cell").text("Recognition").attr("colspan", "3"));

    var dtmfRecognitionConfiguration = recognition.dtmfRecognitionConfiguration;
    var speechRecognitionConfiguration = recognition.speechRecognitionConfiguration;

    processConfiguration(dtmfRecognitionConfiguration, true, interactionSectionTable);
    processConfiguration(speechRecognitionConfiguration, false, interactionSectionTable);

    addProperties(interactionSectionTable, _.omit(recognition, [
      "dtmfRecognitionConfiguration",
      "speechRecognitionConfiguration" ]));

    hasInput = true;
  }

  var recording = interaction.recording;
  if (recording) {
    row = $("<tr>");
    interactionSectionTable.append(row);

    row.append($("<td>").addClass("index-cell").text("Recording").attr("colspan", "3"));

    var dtmfTermConfiguration = recording.dtmfTermConfiguration;
    if (dtmfTermConfiguration) {
      addGrammars(dtmfTermConfiguration.grammars, true, interactionSectionTable);
    }

    addProperties(interactionSectionTable, recording);

    hasInput = true;
  }

}

function processMessageTurn(turn) {
  var message = turn.data;

  $("#messageSection").empty();
  var table = $("<table>");
  $("#messageSection").append(table);

  addProperties(table, _.omit(message, "audioItems"));

  addHeader(table, "audio items");
  var audioTable = $("<table>");
  table.append($("<tr>").append($("<td>").append(audioTable)));

  _.each(message.audioItems, function(audioItem) {
    processAudioItem(audioItem, audioTable);
  });
}

function prepareDialogueStart() {
  servletPath = configuration.uri;
  $("#dialogueStartSection").show();
  $("#startDialogueButton").show();
}

function processReturn(result) {
  $("#inputSection").hide();
  $("#result").text(JSON.stringify(result.data, undefined, "  "));
  prepareDialogueStart();
}

function createVariableInputFields(variables, table) {
  table.find("tbody tr").remove();

  _.each(variables, function(variable) {
    var row = $("<tr>");
    table.append(row);

    var nameCell = $("<td>");
    row.append(nameCell);
    nameCell.text(variable.name);

    var returnedValueCell = $("<td>");
    row.append(returnedValueCell);

    var input = $("<input>");
    returnedValueCell.append(input);

    input.attr("name", variable.name);
    input.attr("value", variable.initialValue);
  });

}

function processScriptExecutionTurn(result) {
  var data = result.data;
  var scriptPre = $("<pre>");
  $("#script").empty();
  $("#script").append(scriptPre);
  scriptPre.text(data.script);

  var variableTable = $("#variables");

  var variables = data.variables;

  variableTable.find("tbody tr").remove();
  _.each(variables, function(variable) {
    var row = $("<tr>");
    variableTable.append(row);

    var nameCell = $("<td>");
    row.append(nameCell);
    nameCell.text(variable.name);

    var initialValueCell = $("<td>");
    row.append(initialValueCell);
    if (variable.initialValue !== undefined) {
      initialValueCell.text(variable.initialValue);
    }
  });

  createVariableInputFields(variables, $("#variableValuesTable"));
}

function processSubdialogueTurn(turn) {

  var turnData = turn.data;

  $("#subdialogueUri").text(turnData.uri);

  if (turnData.submitMethod) {
    $("#subdialogueSubmitMethod").text(turnData.submitMethod);
  } else {
    $("#subdialogueSubmitMethod").text("");
  }

  var submitParameters = turnData.submitParameters;

  var submitParameterTable = $("#subdialogueSubmitParameters");
  if (submitParameters && submitParameters.length > 0) {
    submitParameterTable.find("tbody tr").remove();

    _.each(submitParameters, function(submitParameter) {
      var row = $("<tr>");
      submitParameterTable.append(row);

      var nameCell = $("<td>");
      row.append(nameCell);
      nameCell.text(submitParameter.name);

      var initialValueCell = $("<td>");
      row.append(initialValueCell);
      if (submitParameter.initialValue) {
        initialValueCell.text(submitParameter.initialValue);
      }
    });
    submitParameterTable.show();
  } else {
    submitParameterTable.hide();
  }

  var voiceXmlParameters = turnData.voiceXmlParameters;
  if (voiceXmlParameters && voiceXmlParameters.length > 0) {
    var voiceXmlParameterTable = $("#subdialogueVoiceXmlParameters");
    voiceXmlParameterTable.find("tbody tr").remove();

    _.each(voiceXmlParameters, function(voiceXmlParameter) {
      var row = $("<tr>");
      voiceXmlParameterTable.append(row);

      var nameCell = $("<td>");
      row.append(nameCell);
      nameCell.text(voiceXmlParameter.name);

      var expressionCell = $("<td>");
      row.append(expressionCell);
      if (voiceXmlParameter.expression) {
        expressionCell.text(voiceXmlParameter.expression);
      }

      var valueCell = $("<td>");
      row.append(valueCell);
      if (voiceXmlParameter.value) {
        valueCell.text(voiceXmlParameter.value);
      }
    });

    voiceXmlParameterTable.show();

    $("#voiceXmlParametersDiv").show();
  } else {
    $("#voiceXmlParametersDiv").hide();
  }

  if (turnData.postDialogueScript) {
    $("#postDialogueScriptDiv").show();
    var scriptPre = $("<pre>");
    $("#postDialogueScript").empty();
    $("#postDialogueScript").append(scriptPre);
    scriptPre.text(turnData.postDialogueScript);
  } else {
    $("#postDialogueScriptDiv").hide();
  }
}

function processObjectTurn(turn) {
  var turnData = turn.data;

  addProperties($("#objectDetails"), _.omit(turnData, "parameters"));

  var objectParameters = turnData.parameters;
  if (objectParameters && objectParameters.length > 0) {
    var objectParameterTable = $("#objectParameters");
    objectParameterTable.find("tbody tr").remove();

    _.each(objectParameters, function(objectParameter) {
      var row = $("<tr/>");
      objectParameterTable.append(row);

      row.append($("<td/>").text(objectParameter.name));
      row.append($("<td/>").text(objectParameter.expression || ""));
      row.append($("<td/>").text(objectParameter.value || ""));
      row.append($("<td/>").text(objectParameter.type || ""));
      row.append($("<td/>").text(objectParameter.valueType || ""));

    });

    objectParameterTable.show();

    $("#objectParametersDiv").show();
  } else {
    $("#objectParametersDiv").hide();
  }

  if (turnData.postObjectScript) {
    $("#postObjectScriptDiv").show();
    var scriptPre = $("<pre>");
    $("#postObjectScript").empty();
    $("#postObjectScript").append(scriptPre);
    scriptPre.text(turnData.postObjectScript);
  } else {
    $("#postObjectScriptDiv").hide();
  }
}
function processTransferTurn(turn) {
  var turnData = turn.data;
  var transferSection = $("#transferSection");
  transferSection.empty();
  transferSection.show();
  var table = $("<table>");
  transferSection.append(table);

  addProperties(table, turnData);
}

function processResult(result) {
  clearResult();
  $("#resultSection").hide();
  $("#subdialogueSection").hide();
  $("#objectSection").hide();
  $("#scriptSection").hide();
  $("#transferSection").hide();
  $("#interactionSection").hide();
  $("#messageSection").hide();

  $("#dialogueStartSection").hide();

  var type = result.type;

  if ("servletPath" in result) {
    servletPath = result.servletPath;
  }

  $("#outputSection").show();

  $("#turnName").text(result.turn.name);
  $("#turnType").text(type);

  $("#outputJson").text(JSON.stringify(_.omit(result, "voiceXml"), undefined, "  "));

  if (type === "lastTurn") {
    $("#resultSection").show();
    processReturn(result.turn);
  } else if (type === "outputTurn") {
    var outputTurnType = result.turn.outputTurnType;

    $("#turnType").append(" (" + outputTurnType + ")");

    $("#inputSection").show();
    if (outputTurnType === "interaction") {
      $("#interactionSection").show();
      processInteraction(result.turn);
    } else if (outputTurnType === "message") {
      $("#messageSection").show();
      processMessageTurn(result.turn);
    } else if (outputTurnType === "transfer") {
      $("#transferSection").show();
      processTransferTurn(result.turn);
    } else if (outputTurnType === "subdialogue") {
      $("#subdialogueSection").show();
      processSubdialogueTurn(result.turn);
    } else if (outputTurnType === "object") {
      $("#objectSection").show();
      processObjectTurn(result.turn);
    } else if (outputTurnType === "scriptExecution") {
      $("#scriptSection").show();
      processScriptExecutionTurn(result.turn);
    }
  }

  $("#outputVoiceXml").text(result.voiceXml);
}

function sendInput() {

  var parameters = {
    inputTurn : JSON.stringify(input)
  };

  var file = $("#recording");

  if (file) {
    var recordFormOptions = {
      dataType : "json",
      data : parameters,
      success : processResult
    };

    var form = $("#recordForm");
    form.attr("action", servletPath + "?textarea=true");
    form.ajaxSubmit(recordFormOptions);
  } else {
    processJson(processResult, parameters);
  }
}

function performNoAction() {
  updateResult({}, true);
}

function sendEvent(eventName) {
  var result = {
    events : [ {
      name : eventName,
      message : ""
    } ]
  };
  updateResult(result, true);
}

function removeEvent() {
  if ("events" in input) {
    input.events.pop();
    updateResult(input);
  }
}

function addEvent() {
  var eventName = $("#eventName").val();
  var eventMessage = $("#eventMessage").val();

  if (!("events" in input)) {
    input.events = [];
  }

  input.events.push({
    name : eventName,
    message : eventMessage
  });

  updateResult(input);
}

function performHangUp() {
  sendEvent("connection.disconnect.hangup");
}

function performTransferDisconnect() {
  sendEvent("connection.disconnect.transfer");
}

function performNoMatch() {
  sendEvent("nomatch");
}

function performNoInput() {
  sendEvent("noinput");
}

function performError() {
  sendEvent("error");
}

function addMark() {
  var markName = $("#markInput input[name='markName']").val();
  var markTime = $("#markInput input[name='markTime']").val();

  var mark = {
    name : markName
  };
  if (markTime !== "") {
    mark.time = parseInt(markTime, 10);
  } else {
    mark.time = 0;
  }

  if (!("recognition" in input))
    input.recognition = {};

  input.recognition.mark = mark;
  updateResult(input);
}

function clearMark() {

  if (!("recognition" in input))
    return;

  delete input.recognition.mark;
  updateResult(input);
}
function addHypothesis() {

  var text = $("#recognitionInput input[name='text']").val();
  var interpretation = $("#recognitionInput textarea[name='interpretation']").val();
  var confidenceScore = parseFloat($("#recognitionInput input[name='confidenceScore']").val());
  var inputMode = $('input[name="inputModeRadio"]:checked', '#inputMode').val();

  var hypothesis = {
    utterance : text,
    confidence : confidenceScore,
    inputmode : inputMode
  };

  if (interpretation !== "") {
    try {
      hypothesis.interpretation = JSON.parse(interpretation);
    } catch (error) {
      alertDialogue("Error while parsing interpretation.", error);
    }
  }

  if (!("recognition" in input)) {
    input.recognition = {};
  }

  if (!("result" in input.recognition)) {
    input.recognition.result = [];
  }

  input.recognition.result.push(hypothesis);
  updateResult(input);
}

function sendSingleDtmfHypothesis(dtmf) {
  sendSingleHypothesis("dtmf", dtmf.toString());
}

function sendSingleSpeechHypothesis(dtmf) {
  sendSingleHypothesis("voice", dtmf.toString());
}

function sendSingleHypothesis(mode, value) {

  input = {
    recognition : {
      result : [ {
        utterance : value,
        confidence : 1.0,
        inputmode : mode
      } ]
    }
  };

  updateResult(input, true);
}

function removeHypothesis() {
  if ("recognition" in input) {
    var result = input.recognition.result;
    if (result !== undefined) {
      result.pop();
    }

    updateResult(input);
  }
}

function addValue() {
  try {
    var text = $("#valueInput").val();
    input.value = JSON.parse(text);
    updateResult(input);
  } catch (error) {
    alertDialogue("Error while parsing.", error);
  }
}

function getParameterValue(control) {
  if (control.is(":checkbox")) {
    return control.is(":checked");
  }

  return control.val();
}

function startDialogue() {
  var parameters = {};
  $("#parameterTable").find(":input:enabled").each(function(index, item) {
    item = $(item);
    parameters[item.attr("id")] = getParameterValue(item);
  });

  delete parameters[""];
  processJson(processResult, parameters);

}

function createDropDown(id, options) {
  var select = $("<select>");
  select.attr("id", id);
  $.each(options, function(index, item) {
    select.append("<option>" + item + "</option>");
  });
  return select;
}

function createCheckBox(id, value) {
  var input = $("<input>");
  input.attr("id", id);
  input.attr("type", "checkbox");
  if (value) {
    input.attr("checked", "checked");
  }
  return input;
}

function createTextInput(id, value) {
  var input = $("<input>");
  input.attr("id", id);
  input.attr("type", "text");
  if (value) {
    input.attr("value", value);
  }
  return input;
}

function populateDialogueStartParameters(func) {
  var parameters = $("#parameters");
  parameters.find("table").remove();

  var table = $("<table>");
  parameters.append(table);
  table.attr("id", "parameterTable");
  func(table);
}

function addRow(table, label, control) {
  var tableRow = $("<tr>");
  table.append(tableRow);

  var tableCellCheckbox = $("<td>");
  tableRow.append(tableCellCheckbox);

  var checkbox = $("<input>");
  checkbox.attr("type", "checkbox");
  checkbox.attr("checked", "checked");
  tableCellCheckbox.append(checkbox);

  var tableCellName = $("<td>");
  tableRow.append(tableCellName);
  tableCellName.append(label);

  var tableCellValue = $("<td>");
  tableRow.append(tableCellValue);
  tableCellValue.append(control);

  checkbox.change(function(x) {
    control.attr("disabled", !$(this).attr("checked"));
  });
}

function configurationSelectionChanged() {
  var selectedConfiguration = $("#configurationSelection").val();
  configuration = configurations[selectedConfiguration];
  servletPath = configuration.uri;
  populateDialogueStartParameters(configuration.prepareParameters);
}

function setRecordingAction() {
  var duration = $("#recordingInput input[name='duration']").val();
  if (duration && duration.match(/[0-9]+/)) {
    duration = parseInt(duration, 10);
  } else {
    duration = undefined;
  }

  var recordingMetaData = {
    duration : duration,
    termChar : $("#recordingInput input[name='termChar']").val(),
    maxTime : $("#recordingInput input[name='maxTime']").is(":checked")
  };

  var result = {
    recordingMetaData : recordingMetaData
  };

  input.recordingMetaData = recordingMetaData;
  updateResult(input);
}

function clearRecordingAction() {
  delete input.recordingMetaData;
  resetRecordingFile();
  updateResult(input);
}

function resetRecordingFile() {
  $("#recordForm").each(function() {
    this.reset();
  });

  $("#fileSize").empty();
}

function setTransferResultAction() {
  var duration = $("#transferStatus input[name='duration']").val();
  if (duration && duration.match(/[0-9]+/)) {
    duration = parseInt(duration, 10);
  } else {
    duration = undefined;
  }

  var transferResult = {
    duration : duration,
    status : $("#transferStatusCode").val()
  };

  input.transfer = transferResult;
  updateResult(input);
}

function clearTransferResultAction() {
  delete input.transfer;
  updateResult(input);
}

function clearResult() {
  updateResult({}, false);
  resetRecordingFile();
}

function updateResult(result, autoSend) {
  input = result;
  $("#inputJson").text(JSON.stringify(result, undefined, "  "));

  if (autoSend === undefined)
    autoSend = getParameterValue($("#autoSend"));

  if (autoSend)
    sendInput();
}

function alertDialogue(message1, message2) {
  var dialogue = $("#alertDialogue");
  dialogue.dialog("open");
  dialogue.empty();
  dialogue.append(document.createTextNode(message1));
  dialogue.append("<br/>");
  dialogue.append(document.createTextNode(message2));
}

function openInputEditor() {
  $("#editor").val($("#inputJson").text());
  $("#editor").attr("cols", "80");
  $("#editor").attr("rows", "15");
  $("#editorDialogue").dialog("open");
}

function formatJsonInEditor() {
  var text = $("#editor").val();
  try {
    var parsed = JSON.parse(text);
    $("#editor").val(JSON.stringify(parsed, undefined, "  "));
  } catch (exception) {
    alertDialogue("Error while formatting. " + exception);
  }
}

function initializeButtons() {

  var buttonConfiguration = {
    "hangUpButton" : {
      click : performHangUp,
      icons : {
        primary : "ui-icon-closethick"
      }
    },
    "transferButton" : {
      click : performTransferDisconnect,
      icons : {
        primary : "ui-icon-extlink"
      }
    },
    "noInputButton" : {
      click : performNoInput,
      icons : {
        primary : "ui-icon-cancel"
      }
    },
    "noMatchButton" : {
      click : performNoMatch,
      icons : {
        primary : "ui-icon-help"
      }
    },
    "errorButton" : {
      click : performError,
      icons : {
        primary : "ui-icon-alert"
      }
    },
    "addMarkButton" : {
      click : addMark,
      icons : {
        primary : "ui-icon-circle-plus"
      }
    },
    "clearMarkButton" : {
      click : clearMark,
      icons : {
        primary : "ui-icon-circle-minus"
      }
    },
    "addEventButton" : {
      click : addEvent,
      icons : {
        primary : "ui-icon-circle-plus"
      }
    },
    "removeEventButton" : {
      click : removeEvent,
      icons : {
        primary : "ui-icon-circle-minus"
      }
    },
    "addHypothesisButton" : {
      click : function() {
        addHypothesis();
      },
      icons : {
        primary : "ui-icon-circle-plus"
      }
    },
    "removeHypothesisButton" : {
      click : removeHypothesis,
      icons : {
        primary : "ui-icon-circle-minus"
      }
    },
    "setTransferResultButton" : {
      click : setTransferResultAction,
      icons : {
        primary : "ui-icon-circle-plus"
      }
    },
    "clearTransferResultButton" : {
      click : clearTransferResultAction,
      icons : {
        primary : "ui-icon-circle-minus"
      }
    },
    "editButton" : {
      click : openInputEditor,
      icons : {
        primary : "ui-icon-pencil"
      }
    },
    "sendInputButton" : {
      click : sendInput,
      icons : {
        primary : "ui-icon-circle-check"
      }
    },
    "formatButton" : {
      click : formatJsonInEditor,
      icons : {
        primary : "ui-icon-check"
      }
    },
    "setRecordingButton" : {
      click : setRecordingAction,
      icons : {
        primary : "ui-icon-circle-plus"
      }
    },
    "clearRecordingButton" : {
      click : clearRecordingAction,
      icons : {
        primary : "ui-icon-circle-minus"
      }
    },
    "addValueButton" : {
      click : addValue,
      icons : {
        primary : "ui-icon-circle-plus"
      }
    },
    "startDialogueButton" : {
      click : startDialogue,
      icons : {
        primary : "ui-icon-play"
      }
    },
    "continueButton" : {
      click : performNoAction,
      icons : {
        primary : "ui-icon-seek-next"
      }
    },
    "singleDtmfHypothesisButton" : {
      click : function() {
        sendSingleDtmfHypothesis($("#singleHypothesisText").val());
      }
    },
    "singleSpeechHypothesisButton" : {
      click : function() {
        sendSingleSpeechHypothesis($("#singleHypothesisText").val());
      }
    },
    "quickPanelToggleButton" : {
      icons : {
        primary : "ui-icon-star"
      }
    }
  };

  _(10).times(function(dtmfSymbol) {
    $("#dtmf" + dtmfSymbol).click(function() {
      sendSingleDtmfHypothesis(dtmfSymbol);
    });
  });

  $("#dtmfPound").click(function() {
    sendSingleDtmfHypothesis("#");
  });

  $("#dtmfStar").click(function() {
    sendSingleDtmfHypothesis("*");
  });

  _.each(buttonConfiguration, function(configuration, id) {
    var target = $("#" + id);
    var button = target.button(_.omit(configuration, "click"));
    if (configuration.click !== undefined) {
      button.click(configuration.click);
    }
  });
}

$(function() {

  initializeButtons();
  $("#outputTabs").tabs({
    collapsible : true
  });

  $("#inputTabs").tabs({
    collapsible : true
  });

  $("#inputSection").hide();
  $("#outputSection").hide();

  var configurationSelection = $("#configurationSelection");
  configurationSelection.empty();

  _.each(configurations, function(configuration, name) {
    var option = $("<option>");
    option.attr("value", name);
    option.text(name);
    configurationSelection.append(option);
  });

  configurationSelection.change(configurationSelectionChanged);
  configurationSelectionChanged();

  prepareDialogueStart();

  $("#editorDialogue").dialog({
    width : 800,
    heigth : 500,
    autoOpen : false,
    modal : true,
    title : "Edit Dialogue Input (JSON)",
    buttons : {
      "Ok" : function() {
        try {
          var result = JSON.parse($("#editor").val());
          updateResult(result);
          $(this).dialog("close");
        } catch (error) {
          alertDialogue("Error while parsing.", error);
        }
      },
      "Cancel" : function() {
        $(this).dialog("close");
      }
    }
  });

  $("#quickPanelDialogue").dialog({
    position : {
      my : "left top",
      at : "right+5 top",
      of : "#main",
      collision : "fit"
    },
    title : "Quick Panel",
    close : function() {
      $("#quickPanelToggleButton").removeAttr("checked").change();
    }
  });

  $("#keyShortcuts").on("keypress", function(event) {

    function is(character) {
      return event.charCode == character.toUpperCase().charCodeAt(0) || event.charCode == character.charCodeAt(0);
    }

    if (event.charCode >= 48 && event.charCode <= 57) {
      var dtmfDigit = (event.charCode - 48).toString();
      sendSingleDtmfHypothesis(dtmfDigit);
    } else if (event.charCode == 35) {
      sendSingleDtmfHypothesis("#");
    } else if (event.charCode == 42) {
      sendSingleDtmfHypothesis("*");
    } else if (is('i')) {
      performNoInput();
    } else if (is('m')) {
      performNoMatch();
    } else if (is('h')) {
      performHangUp();
    } else if (is('t')) {
      performTransferDisconnect();
    } else if (is('e')) {
      performError();
    } else if (is(' ') || event.charCode == 13) {
      performNoInput();
    }

    return false;

  });

  $("#longPopupDialogue").dialog({
    width : 800,
    heigth : 500,
    autoOpen : false,
    modal : true,
    title : "Details",
    buttons : {
      "Close" : function() {
        $(this).dialog("close");
      }
    }
  });

  $("#quickPanelToggleButton").change(function() {
    if (getParameterValue($(this))) {
      $("#quickPanelDialogue").dialog("open");
    } else {
      $("#quickPanelDialogue").dialog("close");
    }
  });

  $("#alertDialogue").dialog({
    width : 400,
    heigth : 300,
    autoOpen : false,
    modal : true,
    title : "Alert",
    buttons : {
      "Close" : function() {
        $(this).dialog("close");
      }
    }
  });

  $("#transferStatusCode").autocomplete(
    {
      source : [
        "noanswer",
        "near_end_disconnect",
        "far_end_disconnect",
        "network_disconnect",
        "maxtime_disconnect",
        "busy",
        "network_busy",
        "unknown" ]
    });

  $("#eventName").autocomplete(
    {
      source : [
        "cancel",
        "connection.disconnect",
        "connection.disconnect.hangup",
        "connection.disconnect.transfer",
        "exit",
        "help",
        "noinput",
        "nomatch",
        "maxspeechtimeout",
        "error",
        "error.badfetch",
        "error.badfetch.http",
        "error.badfetch.protocol",
        "error.semantic",
        "error.noauthorization",
        "error.noresource",
        "error.unsupported.builtin",
        "error.unsupported.format",
        "error.unsupported.language",
        "error.unsupported.objectname",
        "error.unsupported",
        "error.connection",
        "error.connection.noauthorization",
        "error.connection.baddestination",
        "error.connection.noroute",
        "error.connection.noresource",
        "error.connection.protocol",
        "error.unsupported.transfer.blind",
        "error.unsupported.transfer.bridge",
        "error.unsupported.transfer.consultation",
        "error.unsupported.uri" ]

    });

  $("#recording").change(function() {
    if (this.files.length > 0) {
      $("#fileSize").text("Size: " + this.files[0].size + " bytes");
    }
  });

  $("#inputMode").buttonset();
});