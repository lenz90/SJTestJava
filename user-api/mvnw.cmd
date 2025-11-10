@echo off
rem Maven wrapper script for Windows

setlocal

set MAVEN_HOME=%~dp0\.mvn\wrapper\maven-wrapper.jar
set MAVEN_OPTS=%MAVEN_OPTS% -Dfile.encoding=UTF-8

java -cp "%MAVEN_HOME%" org.apache.maven.wrapper.MavenWrapperMain %*

endlocal