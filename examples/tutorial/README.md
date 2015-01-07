DeployR Java Client Library Tutorial Examples
=============================================

The DeployR Java client library ships with a number of sample applications
provided to demonstrate some of the key featues introduced by the
[Quick Start Tutorial](http://deployr.revolutionanalytics.com/documents/dev/clientlib)
for the Java client library.

A Gradle build script is provided to run the example applications:

```
build.gradle
```

By default, the build configuration assumes an instance of the DeployR server
is running on `localhost`. If your instance of DeployR is running at some
other IP address then please udpate the `endpoint` property in the
configuration file as appropriate.

You do not need to install Gradle before running these commands. For
example, to run the `Connect` example application on a Unix based OS,
run the following shell script:

```
gradlew run -DtestClass=com.revo.deployr.tutorial.connection.Connect
```

To run the `Connect` example application on a Windows based OS, run the
following batch file:

```
gradlew.bat run -DtestClass=com.revo.deployr.tutorial.connection.Connect

```

The complete list of Client Library Tutorial example applications are as
follows:

```
//
// Getting Connected
//
gradlew run -DtestClass=com.revo.deployr.tutorial.connection.Connect

//
// Authentication
//
gradlew run -DtestClass=com.revo.deployr.tutorial.authentication.Authenticate

//
// Authenticated Services - Project Services
//
gradlew run -DtestClass=com.revo.deployr.tutorial.services.project.AuthProjectCreate
gradlew run -DtestClass=com.revo.deployr.tutorial.services.project.AuthProjectDirectory
gradlew run -DtestClass=com.revo.deployr.tutorial.services.project.AuthProjectExecuteCode
gradlew run -DtestClass=com.revo.deployr.tutorial.services.project.AuthProjectExecuteScript
gradlew run -DtestClass=com.revo.deployr.tutorial.services.project.AuthProjectPackages
gradlew run -DtestClass=com.revo.deployr.tutorial.services.project.AuthProjectPoolCreate
gradlew run -DtestClass=com.revo.deployr.tutorial.services.project.AuthProjectWorkspace

//
// Authenticated Services - Background Job Services
//
gradlew run -DtestClass=com.revo.deployr.tutorial.services.background.AuthJobExecuteCode
gradlew run -DtestClass=com.revo.deployr.tutorial.services.background.AuthJobExecuteScript
gradlew run -DtestClass=com.revo.deployr.tutorial.services.background.AuthJobStoreResultToRepository

//
// Authenticated Services - Repository Services
//
gradlew run -DtestClass=com.revo.deployr.tutorial.services.repository.AuthRepositoryManagement

//
// Anonymous Services
//
gradlew run -DtestClass=com.revo.deployr.tutorial.services.project.AnonProjectExecuteScript
```
