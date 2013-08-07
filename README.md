## Command line

To build from the command line, use the gradlew script: `./gradlew build` on `*NIX` and `gradlew.bat build` on windows. To show a list of the usable tasks, run `gradlew tasks`.

Note: remember that Gradle permits camel-case shortcuts for the task name, as such `teCl` resolves to `testClasses`.

### Configuring the Gradle wrapper

The gradle wrapper can be configured via the gradle-wrapper.properties file, under the gradle/wrapper folder. The most important property is the distributionUrl, which determines which version of Gradle is downloaded. The pattern for the url is: `http://services.gradle.org/distributions/gradle-[versionNumber]-[versionStyle].zip`. The `versionStyle` can be either `bin` or `all`; `bin` only contains the runtime, where `all` also contains the documentation and source. By default, we are using `bin`.

## Eclipse integration

Install the Gradle IDE plugin from [Spring's update site](http://dist.springsource.com/release/TOOLS/gradle). To load the projects inside Eclipse, choose File -> Import -> Gradle -> Gradle Project, select the root directory of the project, click Build Model and finish.

You can also configure which version of Gradle the IDE plugin uses: go to Window -> Preferences -> Gradle and change the URI of Gradle distribution. This is the same URL pattern as the Gradle wrapper.

## Publishing

To "publish" the projects on artifactory, simply run the `publish` task: it will build the projects and push the artifacts at knox. The group is set to `com.nuecho`, version is at `0.1.0` for now and the module name maps to the project name. To reference the published artifact, you declare a dependency on `"com.nuecho:rivr-voicexml:0.1.0"`. For the web interface (aka voicexml dialogue runner), here's the recipe to include it in a dependent webapp:

```groovy
apply plugin: 'war' // Must be a webapp project
configurations { dialogueRunner } // The name of the configuration can be anything
dependencies {
    dialogueRunner 'com.nuecho:dialogue-runner:0.2.5@war'
}
repositories { ivy { url 'your repository here.' } } // Use your favorite repository
war{
    def dialogueRunner = { zipTree(configurations.dialogueRunner.singleFile) } // This enables lazy resolving
    from(dialogueRunner){
        into 'dialogue-runner' // configure where to put in the webapp; remove to simply have it at the root.
    }
}
```

This is the Gradle recipe for war overlaying, simplified for only one file.

## TODO

* Configure Eclipse projects to hide the build dir
* Configure JDT settings from Gradle.
* Revoir la gestion des resources javascript dans rivr-voicexml.
* Better integration with Eclipse wrt webapp development
