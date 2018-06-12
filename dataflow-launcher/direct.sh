#!/usr/bin/env bash

mvn compile exec:java \
    -Dexec.mainClass=org.pentaho.dataflow.launcher.LauncherMain