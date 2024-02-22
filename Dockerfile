FROM openjdk:8-jre-slim

COPY target/notes-0.0.1-SNAPSHOT.jar notes.jar
COPY src/main/resources/application.yml application.yml

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/notes.jar"]