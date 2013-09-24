/*global application */

(function(rivr) {

  rivr.addRecognitionResult = function() {
    rivr.inputTurn.recognition = {
      result : application.lastresult$,
      inputMode : application.lastresult$.inputmode
    };

    if (application.lastresult$.markname) {
      rivr.inputTurn.recognition.mark = {
        name : application.lastresult$.markname,
        time : application.lastresult$.marktime
      };
    }

    if (application.lastresult$.recording) {
      rivr.inputTurn.recordingMetaData = {
        duration : application.lastresult$.recordingduration,
        size : application.lastresult$.recordingsize
      };

      rivr.inputTurn.recordingMetaData.data = application.lastresult$.recording;
    }
  };

  rivr.addRecordingResult = function(field, fieldShadow, addAudio) {
    rivr.inputTurn.recordingMetaData = {
      duration : fieldShadow.duration,
      size : fieldShadow.size,
      termChar : fieldShadow.termchar,
      maxTime : fieldShadow.maxtime
    };

    if (addAudio) {
      rivr.inputTurn.recordingMetaData.data = field;
    }
  };

  rivr.addTransferResult = function(field, fieldShadow) {
    var duration;
    if (fieldShadow) {
      duration = fieldShadow.duration;
    } else {
      duration = 0;
    }

    rivr.inputTurn.transfer = {
      status : field,
      duration : duration
    };
  };

  rivr.addEventResult = function(name, message) {
    if (!rivr.inputTurn.hasOwnProperty("events")) {
      rivr.inputTurn.events = [];
    }

    var event = {
      name : name,
      message : message
    };

    rivr.inputTurn.events.push(event);
  };

  rivr.addValueResult = function(value) {
    rivr.inputTurn.value = value;
  };

  rivr.hasRecording = function(inputTurn) {
    return inputTurn.hasOwnProperty("recordingMetaData") && inputTurn.recordingMetaData.hasOwnProperty("data");
  };

  function serialize(value, cycleDetectionValues) {
    if (!cycleDetectionValues) {
      cycleDetectionValues = [];
    }

    for ( var index = 0; index < cycleDetectionValues.length; index++) {
      if (value === cycleDetectionValues[index]) {
        throw "cycle detected.";
      }
    }

    cycleDetectionValues.push(value);

    var type = typeof value;

    var out = "";

    if (value === null || value === undefined) {
      out = "null";
    } else if (type == "object" && value.constructor == Array) {
      out += serializeArray(value, cycleDetectionValues);
    } else if (type == "object") {
      out += serializeObject(value, cycleDetectionValues);
    } else if (type == "string") {
      out += serializeString(value);
    } else if (type == "number") {
      if (isNaN(value) || value == Infinity || value == -Infinity)
        throw "Invalid number value.";
      out += value;
    } else if (type == "boolean") {
      out += serializeBoolean(value);
    } else {
      // everything else is serialized as null
      out = "null";
    }

    cycleDetectionValues.pop();

    return out;
  }

  function serializeObject(object, cycleDetectionValues) {
    var out = "";

    var propertyNames = [];

    var propertyName;
    for (propertyName in object) {
      if (object.hasOwnProperty(propertyName))
        propertyNames.push(propertyName);
    }

    propertyNames.sort();

    while (undefined !== (propertyName = propertyNames.shift())) {
      if (out !== "") {
        out += ",";
      }

      if ((typeof propertyName) == "string") {
        out += serializeString(propertyName);

        var value = object[propertyName];
        out += ":" + serialize(value, cycleDetectionValues);
      }
    }

    return "{" + out + "}";
  }

  function serializeArray(array, cycleDetectionValues) {
    var out = "";

    for ( var index = 0; index < array.length; index++) {
      if (out !== "") {
        out += ",";
      }

      var value = array[index];
      out += serialize(value, cycleDetectionValues);
    }

    return "[" + out + "]";
  }

  function toHex(number) {
    var numberAsHex = number.toString(16);
    while (numberAsHex.length < 4) {
      numberAsHex = "0" + numberAsHex;
    }
    return numberAsHex;
  }

  function serializeString(string) {
    var out = "";

    for ( var index = 0; index < string.length; index++) {
      var charCode = string.charCodeAt(index);
      if (charCode > 126 || charCode < 32) {
        out += "\\u";
        out += toHex(charCode);
      } else {
        var character = string.charAt(index);
        if (character == '\\' || character == '"') {
          out += '\\';
        }
        out += character;
      }
    }
    return '"' + out + '"';
  }

  function serializeBoolean(booleanValue) {
    return booleanValue ? "true" : "false";
  }

  if (typeof JSON === "object" && JSON.stringify) {
    // Using native JSON object if available
    rivr.toJson = JSON.stringify;
  } else {
    // defaulting to custom JSON serialization function
    rivr.toJson = serialize;
  }

})(application.hasOwnProperty("rivr") ? application.rivr : document.rivr);
