#!/bin/bash

mkdir -p /opt/application/src/main/resources/csv/street
mkdir -p /opt/application/src/main/resources/csv/outcomes

find . | grep ".*-street\.csv" | xargs mv -t /opt/application/src/main/resources/csv/street/

find . | grep ".*-outcomes\.csv" | xargs mv -t /opt/application/src/main/resources/csv/outcomes/

find /opt/application/src/main/resources/ -type d -empty -delete