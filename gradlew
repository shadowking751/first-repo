#!/usr/bin/env sh

# Simple Gradle wrapper launcher for CI

# Resolve directory of this script
APP_HOME=$(cd "${0%/*}" && pwd -P)

# Path to the wrapper JAR
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Pick Java
if [ -n "$JAVA_HOME" ] ; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

# Run Gradle wrapper main class
exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
