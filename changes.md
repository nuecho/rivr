# Rivr change log

## Version 1.0.4:

### Rivr core

#### Changed

* (nothing)

#### Added

* (nothing)

#### Removed

* (nothing)

#### Fixed

* (nothing)

### Rivr VoiceXML

#### Changed

* (nothing)

#### Added

* (nothing)

#### Removed

* (nothing)

#### Fixed

* Fixed [Github issue #1](https://github.com/nuecho/rivr/issues/1) A recording stopped by a connection.disconnect event wasn't added to the result.

## Version 1.0.3

### Rivr core

#### Changed

* (nothing)

#### Added

* New mechanism to query version number of Rivr.
* DialogueServlet: Added loggers for servlet itself and for responses.

#### Removed

* (nothing)

#### Fixed

* DialogueServlet: destroy() called on initialization error.
* DialogueServlet: Stopping session container on shutdown.
* DialogueServlet: Fixed leak caused by MDC (slf4j) stored in a ThreadLocal.

### Rivr VoiceXML

#### Changed

* build.gradle: Updated run-time dependency: org.glassfish:javax.json:1.0.4.

#### Added

* (nothing)

#### Removed

* (nothing)

#### Fixed

* Fixed problem preventing a variable declaration not to have an initial value.

## Version 1.0.2:

### Rivr core

#### Changed

* (nothing)

#### Added

* `ServletResponseContent`: added `getContentLength()`

#### Removed

* (nothing)

#### Fixed

* `DialogueServlet`: Generates the `Content-Length` header.  This prevents chunked encoding on some servlet containers (at least on Oracle WebLogic).  This was causing problem for the dialogue runner.

### Rivr VoiceXML

#### Changed

* `XmlDocumentServletResponseContent` and `JsonServletResponseContent`: added `getContentLength()`

#### Added

* (nothing)

#### Removed

* (nothing)

#### Fixed

* (nothing)

## Version 1.0.1:

### Rivr core

#### Changed

* `DialogueServlet`: Made setters final (might break existing subclasses).

#### Added

* `DialogueServlet`: Added 2 new initial arguments:
    * `com.nuecho.rivr.core.controllerTimeout`
    * `com.nuecho.rivr.core.webappServerSessionTrackingEnabled`

#### Removed

* (nothing)

#### Fixed

* `DialogueServlet`: Fixed whitespace in error message.
* `DialogueServlet`: Fixed init-arg prefix.  Correctly documented as `com.nuecho.rivr.core` but was using `com.nuecho.core.voicexml`.

### Rivr VoiceXML

#### Changed

* `VoiceXmlFirstTurn`: Deprecated method signatures 
    * `getParameter(Object)`
    * `hasParameter(Object)`
* `Message.Builder`: Deprecated `addAudio`

#### Added

* `VoiceXmlFirstTurn`: Added methods with better signatures
    * `getParameter(String)`
    * `hasParameter(String)` 
* `Message.Builder`: Added 2 methods:
    * `addAudioItem(AudioItem)`
    * `addAudioItems(AudioItem...)`

#### Removed

* (nothing)

#### Fixed

* `Interaction`: Added null checks in arrays and collections passed as parameters.
* `VariableList`: Added more checks.
* `Interaction`: Fixed encapsulation issue with `setAcknowledgeAudioItems()`

## Version 1.0.0:

### Rivr core

#### Changed

* `TestDialogueChannel`: Renamed `getLastAsError()` to `getLastStepAsError()`
* `DialogueUtils`: Flipped parameters of `doTurn()`
* Now uses Slf4j API version 1.7.5 
* Renamed `TimeValue` to `Duration`.

#### Added

* Javadoc for 
    * all packages
    * all classes
    * some methods
* `TestDialogueChannel`: `getLastStepAsLastTurn()` method
* `DialogueServlet`: `destroyDialogueServlet()` method

#### Removed

* (nothing)

#### Fixed

* `Assert`: Fixed error message for `notEmpty()`


### Rivr VoiceXML

#### Changed

* Renamed `VariableDeclarationList` to `VariableList`
* Renamed `ObjectTurn` to `ObjectCall`.
* Renamed `SubdialogueInvocationTurn` to `SubdialogueCall`.
* Renamed `ScriptExecutionTurn` to `Script`.
* Renamed `InteractionTurn` to `Interaction`.
* Renamed all `VoiceXmlLastTurn` classes: not using the `VoiceXml` prefix and `Turn` suffix anymore.
* Renamed `MessageTurn` to `Message`.
* Renamed `TransferTurn` to `Transfer` (and similarly for subclasses).
* Merged `ClientSideRecording` into `AudioFile`.
* Renamed `Recording` to `AudioFile`.
* In various classes, `fetchAudio` property is no longer an `AudioFile` but a `String`
* Renamed `SynthesisText` to `SpeechSynthesis`.
* Changed how `multipart/form-data` uploaded files are exposed (affects `Recording` result)
* `VoiceXmlDialogueContext`: initialized `fetchConfiguration` property with an empty object.
* `FetchConfiguration`: initialized properties with empty objects.
* Root document factory does not depend on session anymore.
* Removed `turnName` and `turnIndex` JavaScript variables from generated VoiceXML.
* Moved classes from sub-packages of `com.nuecho.rivr.voicexml.turn.output` to parent package.
* Moved `SubdialogueFetchConfiguration` in `SubdialogueCall`.
* Simplified `FetchHint` enum.
* Simplified `SubmitMethod` enum.
* `SpeechRecognitionConfiguration` and `DtmfRecognitionConfiguration` renamed to `SpeechRecognition` and `DtmfRecognition`.
* Moved `VoiceXmlDialogueContext` to `dialogue` package.
* `Interaction`: Fixed visibility.
* `SubdialogueCall`: removed redundant `SubmitMethod` enum
* Renamed `ObjectParmeter` to `Object.Parameter`
* `Interaction`: Made some methods and constructors public.
* `MarkInfo` now wraps a `Duration` instead of a `long`.
* Changed how `acknowledgeAudioItems` and `noInputTimeout` properties are set using constructors  and setters
  for `Interaction.FinalRecordingWindow` and `Interaction.FinalRecognitionWindow`.
* Renamed `InteractionBuilder` to `Interaction.Builder`. 
* Renamed `Script.script` property to `Script.code`

#### Added

* Javadoc for 
    * all packages
    * all classes
    * some methods
* `equals()` and `hashCode()` methods for:
    * `Interaction.Prompt`
    * `Interaction.FinalRecognitionWindow`
    * `Interaction.FinalRecordingWindow`
    * `Recording`
    * `Recognition`
* Getter methods for some `VoiceXmlLastTurn` classes:
    * `DisconnectTurn.getVariables()`
    * `Exit.getVariables()`
    * `Exit.getExpression()`
    * `Goto.getUri()`
    * `Return.getVariables()`
    * `Return.getEventName()`
    * `Return.getEventMessage()`
    * `Submit.getVariables()`
    * `Submit.getUri()`
* Factory methods in AudioFile:
    * `fromLocation(String location)`
    * `fromLocation(String location, SpeechSynthesis alternate)`
    * `fromLocation(String location, String alternate)`
    * `fromExpression(String expression)`
    * `fromExpression(String expression, SpeechSynthesis alternate)`
    * `fromExpression(String expression, String alternate)`
* `AudioFile`: `path` property renamed to `location` and `alternative` property renamed to `alternate`
* `OutputTurns` class: _fluent_ builders for `OutputTurns`.

#### Removed

* `VariableDeclaration` class

#### Fixed

* Various fixes in `rivr.js` making it more robust against older JavaScript interpreter
* If `VoiceXmlDialogueServlet` is subclassed, `rivr.js` is obtained from `com.nuecho.rivr.voicexml.servlet.scripts` package 
  and not the package of the subclass.
* No more `NullPointerException` in `VoiceXmlStepRenderer` constructor.
* Fixed error message in `VoiceXmlInputTurnFactory` when `inputTurn` parameter is missing.
* `DefaultVoiceXmlRootDocumentFactory`: Proper initialization of `rivr` variable.

-----

First public version is 0.9.2
