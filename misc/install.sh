#!/bin/bash
mvn install:install-file -Dfile=`dirname $0`/thep-0.2.jar -DgroupId=thep \
    -DartifactId=thep -Dversion=0.2 -Dpackaging=jar
