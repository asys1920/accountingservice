FROM openjdk:11-jdk-slim AS builder
WORKDIR target/dependency
ARG APPJAR=target/*.jar
COPY ${APPJAR} app.jar
RUN jar -xf ./app.jar

FROM docker.elastic.co/beats/filebeat:7.5.1
COPY dockerfiles/filebeat.yml /usr/share/filebeat/filebeat.yml

FROM openjdk:11-jre-slim
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.asys1920.service.AccountingServiceApplication"]