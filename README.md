Java Client Library for DeployR
===============================

The [DeployR API](https://github.com/deployr/server) exposes a wide range of
R analytics services to client application developers. These services are
exposed using standards-based JSON/XML and are delivered by the DeployR 
server as Web services over HTTP(S).

The DeployR Java client library is provided to simplify the integration of
DeployR services within Java client applications.

Links
-----

  * [Download](https://github.com/deployr/java-client-library/releases)
  * [Quick Start Tutorial](http://deployr.revolutionanalytics.com/documents/dev/clientlib)
  * [Client Library API JavaDoc](http://deployr.github.io/java-client-library/)
  * [DeployR API Reference Guide](http://deployr.revolutionanalytics.com/documents/dev/api-doc/guide)
  * [Library Dependencies](#dependencies)
  * [Example Code](#examples)
  * [License](#license)

Dependencies
============


Declarative JAR Dependencies: Maven Central Repository Artifacts
----------------------------------------------------------------

Artifacts for each official release (since 7.3.0) of the DeployR Java client
library is published to the Maven Central repository.

[ArtifactId](http://search.maven.org/#search|ga|1|a%3A%22jDeployR%22): `jDeployR`

Using build frameworks such as Maven and Gradle your Java client
application can simply declare a dependency on the appropriate version
of the `jDeployR` artifact to ensure all required JAR dependencies are resolved
and available at runtime.


Bundled JAR Dependencies
------------------------

If you are not defining your DeployR client library JAR dependencies using
declarative tools then you must ensure the required JAR files are placed
directly on your application CLASSPATH.

Besides the DeployR Java client library JAR itself, `jDeployR-<version>.jar`,
there are a number of 3rd party JAR file dependencies. These additional JAR
file dependencies are provided for your convenience in the `lib` directory
within this repository.


Building the Java Client Library
--------------------------------

A Gradle build script is provided to build the DeployR Java client
library:

```
build.gradle
```

By default, the build will generate a version of the  `jDeployR-<version>.jar`
file in the `build/libs` directory.

You do not need to install Gradle before running these commands. To
build the DeployR Java client library a Unix based OS, run the following shell
script:

```
gradlew build
```

To build the DeployR Java client library on a Windows-based OS, run the following
batch file:

```
gradlew.bat build
```


Examples
========

The DeployR Java client library ships with a number of sample applications
provided to demonstrate some of the key features introduced by the
[Quick Start Tutorial](http://deployr.revolutionanalytics.com/documents/dev/clientlib)
for the Java client library. See
[here](examples/tutorial) for details.

The DeployR Java client library also ships with a set of unit tests. See 
[here](test) for details.

License
=======

Copyright (C) 2010-2016, Microsoft Corporation

This program is licensed to you under the terms of Version 2.0 of the
Apache License. This program is distributed WITHOUT
ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
Apache License 2.0 (http://www.apache.org/licenses/LICENSE-2.0) for more 
details.
