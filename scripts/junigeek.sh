#!/bin/bash


## TODO: speed up loading time  by reducing jar subset
CLASSPATH=/opt/junigeek/lib/*
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/

echo $CLASSPATH

 java -Dfile.encoding=UTF-8 -cp "/opt/junigeek/lib/*" juniter.Application --spring.config.location=/opt/junigeek/conf/application.yml

