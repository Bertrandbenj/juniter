#!/bin/bash

VOWL=/opt/juniterriens/WebVOWL
EXEC=`grunt webserver`

if test -f ${VOWL}; then
    echo "===================== INSTALL Web2Vowl ====================="
    cd /opt/juniterriens/
    git clone https://github.com/VisualDataWeb/WebVOWL
    cd ${VOWL}
    npm install
    cd ..
    sudo mv WebVOWL /opt/juniterriens/
else

echo "===================== grunt webserver ====================="
    cd ${VOWL}
    grunt webserver
fi



