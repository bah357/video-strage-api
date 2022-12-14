FROM openjdk:17-jdk

WORKDIR /data

RUN useradd -u 1000 -U app && \
    chown -R app:app /data

USER app
COPY ./build/libs/Video-Storage-Server-API-1.0.jar /data/

ARG GIT_HASH

ENV GIT_HASH $GIT_HASH

EXPOSE 8080

CMD ["java", "-Xmx1024m", "-XX:MaxMetaspaceSize=256m", "-jar", "Video-Storage-Server-API-1.0"]

