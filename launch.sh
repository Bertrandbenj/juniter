#!/bin/bash

check_var(){
  if [ -z "$1" ]; then
    >&2 echo "error: missing environmental variable $2. exit"
    exit 1
  fi
}

check_var "${JAVA_XMS}" "JAVA_XMS"
check_var "${JAVA_XMX}" "JAVA_XMX"
check_var "${APP_TIMEZONE}" "APP_TIMEZONE"

XMS=${JAVA_XMS:-none}
XMX=${JAVA_XMX:-none}
TIMEZONE=${APP_TIMEZONE:-none}

JAVA_OPTS="-Xmx${XMX} -Xms${XMS} -Duser.timezone=${TIMEZONE} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} ${OPTIONS}"

java ${JAVA_OPTS} -jar /juniter.jar