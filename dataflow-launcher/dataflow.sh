#!/usr/bin/env bash

mvn compile exec:java \
    -Dexec.mainClass=org.pentaho.dataflow.launcher.LauncherMain \
    -Dexec.args="--project=dataflow-etl \
    --stagingLocation=gs://[__GS_BUCKET_ID__]/staging/ \
    --output=gs://[__GS_BUCKET_ID__]/output \
    --runner=DataflowRunner"