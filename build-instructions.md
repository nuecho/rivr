## How to build Rivr

The build tool used in Rivr is [Gradle](http://www.gradle.org/). This distribution has been tested with Gradle 1.9. To build, simply do

        gradle build 

Gradle will download the required build dependencies from Maven Central:

* javax.servlet:servlet-api:2.5
* org.slf4j:slf4j-api:1.7.5
* commons-fileupload:commons-fileupload:1.2.1
* javax.json:javax.json-api:1.0

After a successful build, The resulting jar file can be found here:

* rivr-core/build/libs/rivr-core-1.0.0.jar
* rivr-voicexml/build/libs/rivr-voicexml-1.0.0.jar

The javadoc jar files:

* rivr-core/build/libs/rivr-core-1.0.0-javadoc.jar
* rivr-voicexml/build/libs/rivr-voicexml-1.0.0-javadoc.jar

The source jar files:

* rivr-core/build/libs/rivr-core-1.0.0-sources.jar
* rivr-voicexml/build/libs/rivr-voicexml-1.0.0-sources.jar
 

### Gradle wrapper

If you don't have Gradle installed, you can simply use the _Gradle wrapper_ provided with this source distribution: 

(unix):

        ./gradlew build  

(windows):

        gradlew.bat build

The `gradlew` script will download Gradle in your user directory (i.e. your home) and use it from there. Of course, following invocations of `gradlew` will
not trigger the download of Gradle.

NOTE: The gradle wrapper can be configured via the gradle-wrapper.properties file, under the gradle/wrapper folder. The most important
property is the distributionUrl, which determines which version of Gradle is downloaded. The pattern for the url
is: `http://services.gradle.org/distributions/gradle-[versionNumber]-[versionStyle].zip`. The `versionStyle` can be
either `bin` or `all`; `bin` only contains the runtime, where `all` also contains the documentation and source. By default, we are using `bin`.

## Eclipse integration

Install the Gradle IDE plugin from [Spring's update site](http://dist.springsource.com/release/TOOLS/gradle). To load the projects 
inside Eclipse, choose File -> Import -> Gradle -> Gradle Project, select the root directory of the project, click Build Model and finish.

You can also configure which version of Gradle the IDE plugin uses: go to Window -> Preferences -> Gradle and change 
the URI of Gradle distribution. This is the same URL pattern as the Gradle wrapper.

The Rivr subprojects (rivr-core, rivr-voicexml, rivr-voicexml-dialogue-runner) can also be imported into Eclipse without
using the Gradle plugin. In order to to so, you must generate the corresponding `.project` files. Simply run `gradlew eclipse`.  

NOTE: If later on you install the Eclipse Gradle plugin, you can convert the projects into Gradle projects by applying the Gradle nature:

* Right-click on the project
* Select Configure > Convert to Gradle Project
* Project should build and library references and such updated.  

## How to make a project depend on Rivr

### With Gradle

```groovy
dependencies {
    compile 'com.nuecho:rivr-voicexml:1.0.0'
}
```

### With Maven

```xml
<dependency>
    <groupId>com.nuecho</groupId>
    <artifactId>rivr-voicexml</artifactId>
    <version>1.0.0</version>
</dependency>
```

### With Ivy

```xml
<dependency org="com.nuecho" name="rivr-voicexml" rev="1.0.0"/>
```

### Without dependency manager (Ant)

Obtain and add to your compilation classpath the following jar files:

* commons-fileupload-1.2.1.jar
* javax.json-api-1.0.jar
* servlet-api-2.5.jar
* slf4j-api-1.7.5.jar

In your runtime classpath (i.e. your WEB-INF/lib), you should have:

* rivr-voicexml-1.0.0.jar
* rivr-core-1.0.0.jar
* commons-fileupload-1.2.1.jar
* javax.json-api-1.0.jar
* slf4j-api-1.7.5.jar
* (an slf4j implementation adapter)
* javax.json-ri.jar (can get it from GlassFish project) 

## Using the dialogue runner

The dialogue runner is a HTML interface to simulate VoiceXML input for a Rivr application.  It is packaged as 
a _partial_ WAR file.  All you need to do is to extract it (unzip) over your web project.

If you use Gradle, you can add the following in your `build.gradle` file:

```groovy
apply plugin: 'war' // Must be a webapp project

configurations { dialogueRunner } 

dependencies {
    dialogueRunner 'com.nuecho:rivr-voicexml-dialogue-runner:1.0.0@war'
}

repositories { mavenCentral() }

war {
    def dialogueRunner = { zipTree(configurations.dialogueRunner.singleFile) } // This enables lazy resolving
    from(dialogueRunner)
}
```

(See the Gradle documentation for more information on war overlaying.)
