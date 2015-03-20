## Overview

Rivr is a lightweight open-source dialogue engine enabling Java developers to easily create enterprise-grade VoiceXML applications.

Read our [Getting Started](https://github.com/nuecho/rivr/wiki/Getting-Started) to learn more.

The complete [Javadoc for Rivr](http://nuecho.github.io/rivr/javadoc/) is available online. 

You can also get started by trying some of the Rivr sample applications:

- [Hello World](https://github.com/nuecho/rivr-cookbook/wiki/Hello-World) - a very simple hello world application
- [Voicemail](http://github.com/nuecho/rivr-voicemail) - a prototype voicemail application 

You can continue to learn by example with the [Rivr cookbook](https://github.com/nuecho/rivr-cookbook/wiki).

## FAQ

### What is Rivr?

Rivr is a Java library for VoiceXML application development. The developer writes the dialogue as a normal Java program and Rivr takes care of generating VoiceXML documents dynamically during execution and makes user responses available to the dialogue in a synchronous manner.

Rivr is a Java-centric approach. All Java tools and practice can be applied to IVR application development.

### What is VoiceXML?

VoiceXML is a W3C standard for interactive voice response, i.e. telephony system interacting with the caller by using speech recognition, DTMF input, recording, speech synthesis, etc. 

VoiceXML is primarily targeted at contact center environments and over-the-phone self-service applications.

[VoiceXML 2.0](http://www.w3.org/TR/voicexml20/) is the specification major version while [VoiceXML 2.1](http://www.w3.org/TR/voicexml21/) only adds a few more features on top of the 2.0 version. 

### What is required to _develop_ a VoiceXML application with Rivr?

You should have a Java development environment.  Also, you should already be familiar with the Java language and Java Servlets. While not essential at the beginning, it can be very useful to understand some basic notions of VoiceXML such as prompt queuing, barge-in, properties, etc.  Since the Rivr model is based on VoiceXML, it is sometimes necessary to understand the VoiceXML layer underneath. 

### What is required to _run_ a VoiceXML application with Rivr?

You should have:

1. A VoiceXML-compliant platform (VoiceXML 2.1)
2. A Java web application server (or minimally a _Java Servlet container_)
3. A Java web application (i.e. a WAR file) containing:
    1. the Rivr jar files (rivr-core.jar, rivr-voicexml.jar)
    2. the run-time dependencies
        1. slf4j-api.jar, an SLF4J adapter jar for a given logging framework and the required logging framework jar files.  
        2. commons-fileupload.jar
        3. javax.json-api.jar and an implementation (see GlassFish project for the reference implementation)
    3. your Rivr application (minimally a Dialogue class)
    4. the appropriate configuration in web.xml

### What benefits Rivr offers?

#### Rivr allows Java developers to write callflows as programs. 

The callflow logic is expressed directly in the code. For example, if the call flow required a question to be asked no more than 3 times, this can be implemented with a simple `for` loop.  No need to fiddle with the VoiceXML Form Interpretation Algorithm (FIA).

#### All application logic in centralized in the Java code on the server-side.

With Rivr, no dialogue logic resides on the VoiceXML side.  Dialogue rules can be expressed and controlled from the Java side.  The dialogue state is maintained on the server.

#### Rivr allows unit and coverage testing.

Since Rivr dialogues are regular Java methods, they can be unit tested as any other regular Java code.  It is simple to check with JUnit that a dialogue asks the expected questions and reacts correctly for any simulated user input. By combining the unit tests with a code coverage tool, we can rapidly setup an automated call flow coverage verification solution.   

#### Development of application can start early in the project, even before VoiceXML platform is ready.

Development can start as soon as the dialogue specification is available. Rivr offers a VoiceXML simulation tool, _the dialogue runner_, which allows developers to interactively test the dialogues they are developing.  Unit testing can also starts as soon as we have a working dialogue (which can be within minutes). 

#### Dialogue abstraction, modularity and reuse.

The fact that a dialogue is pure Java code, it's easy to make them abstract.  For example, one can define a dialogue as a Java method taking input parameters which will condition the dialogue execution.  Those dialogues can be placed into reusable Java packages and shared between applications. 

It's even possible to define meta-dialogues, i.e. high-order dialogue composing dialogues together. This level of abstraction is very hard to obtain when using VoiceXML directly but is easily achieved with Rivr.

#### No additional tools required

Rivr only requires standard Java tools, no special software or other design-time environment.  Java already offers tons of tools that can be applied to the Rivr dialogues: debuggers, profilers, coverage tools, javadoc, etc.

#### Flexibility

Rivr is designed not to get in your way. It can be integrated with any enterprise framework or other existing framework (like Spring).  Many points of control has been defined in Rivr, you are never stuck.  You can provide your own implementations for many concepts and you can override many classes to fit your custom context.   

Rivr even works with VoiceXML proprietary extensions.  You can customize generated VoiceXML as required by your VoiceXML platform and exploit vendor-specific features. There are a few ways to do that.

### How to get some support or report a bug?

We have an email address for limited support: [rivr-support@nuecho.com](mailto:rivr-support@nuecho.com)

Also, to report a problem, you can [open an issue on GitHub](https://github.com/nuecho/rivr/issues)

### How to submit a patch?

Send us your pull request on GitHub. It will then be evaluated.  Possibly, we will offer a work-around rather than modifying the code base.  We will not modify the VoiceXML generation to fit a particular platform unless it can be done in compliance with the VoiceXML 2.1 spec.
  