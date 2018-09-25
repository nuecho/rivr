## How to build Rivr

The build tool used in Rivr is [Gradle](https://www.gradle.org/). This distribution requires Gradle 4.5.1 (or more recent). To build, simply do

        gradle build 

Gradle will download the required build dependencies from Maven Central:

* javax.servlet:servlet-api:2.5
* org.slf4j:slf4j-api:1.7.5
* commons-fileupload:commons-fileupload:1.2.1
* javax.json:javax.json-api:1.0

After a successful build, The resulting jar file can be found here:

* rivr-core/build/libs/rivr-core-1.0.10.jar
* rivr-voicexml/build/libs/rivr-voicexml-1.0.10.jar

The javadoc jar files:

* rivr-core/build/libs/rivr-core-1.0.10-javadoc.jar
* rivr-voicexml/build/libs/rivr-voicexml-1.0.10-javadoc.jar

The source jar files:

* rivr-core/build/libs/rivr-core-1.0.10-sources.jar
* rivr-voicexml/build/libs/rivr-voicexml-1.0.10-sources.jar
 

### Gradle wrapper

If you don't have Gradle installed, you can simply use the _Gradle wrapper_ provided with this source distribution: 

(unix):

        ./gradlew build  

(windows):

        gradlew.bat build

The `gradlew` script will download Gradle in your user directory (i.e. your home) and use it from there. Of course, following invocations of `gradlew` will not trigger the download of Gradle.

NOTE: The gradle wrapper can be configured via the gradle-wrapper.properties file, under the gradle/wrapper folder. The most important property is the distributionUrl, which determines which version of Gradle is downloaded. The pattern for the URL is: `https://services.gradle.org/distributions/gradle-[versionNumber]-[versionStyle].zip`. The `versionStyle` can be either `bin` or `all`; `bin` only contains the runtime, where `all` also contains the documentation and source. By default, we are using `bin`.

## Eclipse integration

Development under Eclipse requires [BuildShip](https://projects.eclipse.org/projects/tools.buildship). 

To import Rivr in Eclipse:

* Select `Import...` from the `File` menu  
* Select `Gradle` -> `Gradle Project` 
* Choose the Rivr root directory
* Click Finish

Note: the Gradle Wrapper option can be selected in order to use the recommended Gradle version (4.5.1).

The Rivr subprojects (rivr-core, rivr-voicexml, rivr-voicexml-dialogue-runner) can also be imported into Eclipse without using the Gradle plugin. In order to to so, you must generate the corresponding `.project` files. Simply run `gradlew eclipse`.  

## How to make a project depend on Rivr

### With Gradle

```groovy
dependencies {
    compile 'com.nuecho:rivr-voicexml:1.0.10'
}
```

### With Maven

```xml
<dependency>
    <groupId>com.nuecho</groupId>
    <artifactId>rivr-voicexml</artifactId>
    <version>1.0.10</version>
</dependency>
```

### With Ivy

```xml
<dependency org="com.nuecho" name="rivr-voicexml" rev="1.0.10"/>
```

### Without dependency manager (Ant)

Obtain and add to your compilation classpath the following jar files:

* commons-fileupload-1.2.1.jar
* javax.json-api-1.0.jar
* servlet-api-2.5.jar
* slf4j-api-1.7.5.jar

In your runtime classpath (i.e. your WEB-INF/lib), you should have:

* rivr-voicexml-1.0.10.jar
* rivr-core-1.0.10.jar
* commons-fileupload-1.2.1.jar
* javax.json-api-1.0.jar
* slf4j-api-1.7.5.jar
* (an slf4j implementation adapter)
* javax.json-ri.jar (can get it from GlassFish project) 

## Using the dialogue runner

The dialogue runner is a HTML interface to simulate VoiceXML input for a Rivr application.  It is packaged as a _partial_ WAR file.  All you need to do is to extract it (unzip) over your web project.

If you use Gradle, you can add the following in your `build.gradle` file:

```groovy
apply plugin: 'war' // Must be a webapp project

configurations { dialogueRunner } 

dependencies {
    dialogueRunner 'com.nuecho:rivr-voicexml-dialogue-runner:1.0.10@war'
}

repositories { mavenCentral() }

war {
    def dialogueRunner = { zipTree(configurations.dialogueRunner.singleFile) } // This enables lazy resolving
    from(dialogueRunner)
}
```

(See the Gradle documentation for more information on war overlaying.)
