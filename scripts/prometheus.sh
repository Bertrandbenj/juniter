#!/bin/bash

CONF=/opt/juniterriens/conf/prometheus.yml
EXEC=/opt/juniterriens/prometheus-2.15.1.linux-amd64/prometheus

if test -f "EXEC"; then
    EXEC --config.file=${CONF}
    /opt/juniterriens/prometheus-2.15.1.linux-amd64/prometheus --config.file=${CONF}
else
    echo "===================== INSTALL PROMETHEUS ====================="
    wget https://github.com/prometheus/prometheus/releases/download/v2.15.1/prometheus-2.15.1.linux-amd64.tar.gz
    tar -xf prometheus-2.15.1.linux-amd64.tar.gz
    sudo mv prometheus-2.15.1.linux-amd64/ /opt/juniterriens/
fi
