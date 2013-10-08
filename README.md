## Overview

Rivr is a lightweight open-source dialogue engine enabling Java developers to easily create enterprise-grade VoiceXML applications.

Read our [Getting Started](https://github.com/nuecho/rivr/wiki/Getting-Started) to learn more.

You can also get started by trying some of the Rivr sample applications:

- [Hello World](https://github.com/nuecho/rivr-cookbook/tree/hello-world) - a very simple hello world application
- [Voicemail](http://github.com/nuecho/rivr-voicemail) - a prototype voicemail application 
- [SpeechTEK 2013 Demo](http://github.com/schemeway/rivr-speechtek-demo)

You can continue to learn by example with the [Rivr cookbook](https://github.com/nuecho/rivr-cookbook/wiki).

## How to build

To build the library from the command line, use the gradlew script: `./gradlew build` on `*NIX` and `gradlew.bat build` on windows. To show a list of the usable tasks, run `gradlew tasks`.

Note: remember that Gradle allows camel-case shortcuts for the task name, as such `teCl` resolves to `testClasses`.

### Configuring the Gradle wrapper

The gradle wrapper can be configured via the gradle-wrapper.properties file, under the gradle/wrapper folder. The most important property is the distributionUrl, which determines which version of Gradle is downloaded. The pattern for the url is: `http://services.gradle.org/distributions/gradle-[versionNumber]-[versionStyle].zip`. The `versionStyle` can be either `bin` or `all`; `bin` only contains the runtime, where `all` also contains the documentation and source. By default, we are using `bin`.

## Eclipse integration

Install the Gradle IDE plugin from [Spring's update site](http://dist.springsource.com/release/TOOLS/gradle). To load the projects inside Eclipse, choose File -> Import -> Gradle -> Gradle Project, select the root directory of the project, click Build Model and finish.

You can also configure which version of Gradle the IDE plugin uses: go to Window -> Preferences -> Gradle and change the URI of Gradle distribution. This is the same URL pattern as the Gradle wrapper.

The Rivr subprojects (rivr-core and rivr-voicexml) can also be imported into Eclipse without using the Gradle plugin. In order to to so, you must generate the corresponding `.project` files. Simply run `gradlew eclipse`.

## Dependency declaration

To reference the published artifact from another project, you declare a dependency on `"com.nuecho:rivr-voicexml:0.9.2"`:

```groovy
dependencies {
    compile 'com.nuecho:rivr-voicexml:0.9.2'
}
```

## Dialogue runner


To include the dialogue runner (web interface to simulate VoiceXML) in your Rivr application, include the following in your `build.gradle` file:

```groovy
apply plugin: 'war' // Must be a webapp project
configurations { dialogueRunner } 
dependencies {
    dialogueRunner 'com.nuecho:dialogue-runner:0.9.2@war'
}
repositories { mavenCentral() }
war {
    def dialogueRunner = { zipTree(configurations.dialogueRunner.singleFile) } // This enables lazy resolving
    from(dialogueRunner)
}
```

(See the Gradle documentatin for more information on war overlaying.)
