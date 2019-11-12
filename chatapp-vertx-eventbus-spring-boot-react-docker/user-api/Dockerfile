FROM openjdk:8-jdk-alpine
RUN mkdir /app
WORKDIR /app
ARG JAR_FILE=/user-api/target/user-api-1.0-SNAPSHOT.jar
ARG APP_PROP=docker.application.properties
COPY ${JAR_FILE} app.jar
COPY ${APP_PROP} application.properties
ENTRYPOINT ["java","-jar","app.jar","--spring.config.location=file:/app/application.properties"]