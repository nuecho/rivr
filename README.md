![Build Status](http://ci.s.nuecho.com:8080/job/rivr/badge/icon)

## Command line

To build from the command line, use the gradlew script: `./gradlew build` on *NIX and `gradlew.bat build` on windows. To show a list of the usable tasks, run `gradlew tasks`.

Note: remember that Gradle permits camel-case shortcuts for the task name, as such `teCl` resolves to `testClasses`.

### Configuring the Gradle wrapper

The gradle wrapper can be configured via the gradle-wrapper.properties file, under the gradle/wrapper folder. The most important property is the distributionUrl, which determines which version of Gradle is downloaded. The pattern for the url is: `http://services.gradle.org/distributions/gradle-[versionNumber]-[versionStyle].zip`. The `versionStyle` can be either `bin` or `all`; `bin` only contains the runtime, where `all` also contains the documentation and source. By default, we are using `bin`.

## Eclipse integration

Install the Gradle IDE plugin from [Spring's update site](http://dist.springsource.com/release/TOOLS/gradle). To load the projects inside Eclipse, choose File -> Import -> Gradle -> Gradle Project, select the root directory of the project, click Build Model and finish.

You can also configure which version of Gradle the IDE plugin uses: go to Window -> Preferences -> Gradle and change the URI of Gradle distribution. This is the same URL pattern as the Gradle wrapper.

## TODO

* Configure Eclipse projects to hide the build dir
* Configure JDT settings from Gradle.
* Revoir la gestion des resources javascript dans rivr-voicexml.
* Better integration with Eclipse wrt webapp development