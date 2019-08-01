#!/bin/bash


CLASSPATH=/opt/juniterriens/lib/*
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/

 CONF=" --spring.config.location=/opt/juniterriens/conf/application.yml"

echo $CLASSPATH

java -Dfile.encoding=UTF-8 -cp "/opt/juniterriens/lib/*" juniter.GUIApplication ${CONF}

#--module-path /usr/share/openjfx/lib/ --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.media,javafx.graphics
