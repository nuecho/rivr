## Command line

To build from the command line, use the gradlew script: `./gradlew build` on *NIX and `gradlew.bat build` on windows. To show a list of the usable tasks, run `gradlew tasks`.

Note: remember that Gradle permits camel-case shortcuts for the task name, as such `teCl` resolves to `testClasses`.

## Eclipse integration

Install the Gradle IDE plugin from [Spring's update site](http://dist.springsource.com/release/TOOLS/gradle). To load the projects inside Eclipse, choose File -> Import -> Gradle -> Gradle Project, select the root directory of the project, click Build Model and finish.  

## TODO

* Fix Checkstyle integration
* Configure Eclipse projects to hide the build dir
* Use knox instead of mavenCentral
* Configure JDT settings from Gradle.