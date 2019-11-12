FROM openjdk:8-jdk-alpine
RUN mkdir /app
WORKDIR /app
ARG JAR_FILE=./web/target/web-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]