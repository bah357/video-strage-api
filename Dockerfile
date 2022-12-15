FROM openjdk:17-jdk

WORKDIR /data

RUN useradd -u 1000 -U app && \
    chown -R app:app /data

USER app
COPY ./build/libs/Video-Storage-Server-API-1.0.jar /data/

EXPOSE 8080

CMD ["java", "-Xmx1024m", "-XX:MaxMetaspaceSize=256m", "-jar", "Video-Storage-Server-API-1.0.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "Video-Storage-Server-API-1.0.jar"]

