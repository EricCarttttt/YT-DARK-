#!/bin/sh
##############################################################################
# Gradle start up script for UN*X
# YTDARK | BY SORENUS15K
##############################################################################

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Resolve links: $0 may be a link
PRG="$0"
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

# Check for gradle-wrapper.jar
if [ ! -f "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "gradle-wrapper.jar not found. Attempting to download..."
    WRAPPER_JAR_URL="https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar"
    if command -v curl > /dev/null 2>&1; then
        curl -L -o "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$WRAPPER_JAR_URL"
    elif command -v wget > /dev/null 2>&1; then
        wget -O "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$WRAPPER_JAR_URL"
    else
        echo "ERROR: Neither curl nor wget found. Please install one of them, or manually download gradle-wrapper.jar"
        echo "       from https://github.com/gradle/gradle/raw/v8.7.0/gradle/wrapper/gradle-wrapper.jar"
        echo "       and place it at: $APP_HOME/gradle/wrapper/gradle-wrapper.jar"
        if command -v gradle > /dev/null 2>&1; then
            echo "Falling back to system gradle..."
            exec gradle "$@"
        fi
        exit 1
    fi
fi

exec "$JAVACMD" \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
