#!/bin/bash

VOWL=/opt/juniterriens/WebVOWL
W2W=/opt/juniterriens/OWL2VOWL
EXEC=`grunt webserver`

if test -f ${VOWL}; then
    echo "===================== INSTALL Web2Vowl ====================="
    cd /opt/juniterriens/
    git clone https://github.com/VisualDataWeb/WebVOWL
    cd ${VOWL}
    npm install
    cd ..
    sudo mv WebVOWL /opt/juniterriens/
fi

if test -f ${W2W}; then
    echo "===================== INSTALL OWL2VOWL ====================="
    cd /opt/juniterriens/
    git clone https://github.com/VisualDataWeb/OWL2VOWL
    cd /opt/juniterriens/OWL2VOWL
    mvn package -P war-release -DskipTests
    cd ..
    sudo mv WebVOWL /opt/juniterriens/
fi

if test -f ${VOWL}  && test -f ${W2W}; then
    echo "===================== grunt webserver ====================="
    cd ${VOWL}
    grunt webserver
fi



