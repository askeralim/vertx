FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=message-api-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]