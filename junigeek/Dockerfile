FROM alpine:3.8 AS builder

WORKDIR /opt
ARG JDK_TAR=openjdk-11+28_linux-x64-musl_bin.tar.gz
ARG JDK_DOWNLOAD_PREFIX=https://download.java.net/java/early_access/alpine/28/binaries

RUN wget -q "$JDK_DOWNLOAD_PREFIX/$JDK_TAR" && \
    wget -q "$JDK_DOWNLOAD_PREFIX/$JDK_TAR.sha256"

RUN cat $JDK_TAR.sha256 | xargs -I{} echo "{}  $JDK_TAR" | sha256sum -c - && \
    tar zxf "$JDK_TAR" && \
    ln -s jdk-11 java && \
    rm -f "$JDK_TAR" "$JDK_TAR.sha256"

ENV JAVA_HOME=/opt/java
ENV PATH="$PATH:$JAVA_HOME/bin"

WORKDIR /root/dev
ENV GRADLE_USER_HOME=/root/dev/.cache
RUN mkdir -p /root/dev/.cache

COPY build.gradle gradlew gradlew.bat settings.gradle gradle.properties /root/dev/
COPY gradle /root/dev/gradle

RUN ./gradlew --no-daemon --refresh-dependencies --version

COPY src ./src
RUN ./gradlew --no-daemon build

# Make sure only 1 jar file is assembled
RUN test $(find ./build/libs -type f -name '*.jar' | wc -l) -eq 1

RUN jlink \
    --verbose \
    --module-path "$JAVA_HOME/jmods" \
    --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.management \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --strip-debug \
    --output /opt/jre

FROM alpine:3.8
WORKDIR /usr/src/app

ENV JAVA_HOME=/opt/jre
ENV LANG=C.UTF-8
ENV PATH="$PATH:$JAVA_HOME/bin"
ENV APP_TIMEZONE=UTC

COPY --from=builder /opt/jre /opt/jre
COPY --from=builder /root/dev/build/libs/*.jar dagger.jar

COPY newrelic/newrelic.jar newrelic/newrelic.yml ./

EXPOSE 80

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "dagger.jar"]