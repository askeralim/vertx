# Vert.x Application Experiments
### Background
This application implements the basic dockerisation of a Vert.x application with ReactApp, It integrates the Vert.x EventBus to communicate between clients through Vert.x server with a cluster host.

### GIT Link
```
git clone https://github.com/askeralim/vertx.git
```
As the project have multiple projects change directory to the vertx-react-chatapp-docker directory
```
cd vertx-react-chatapp-docker
```
Build the application 
```
mvn clean package
```
Create the docker Image using following command.
```
docker build -t chat-app/vertx-java .
```
Run the docker Image using the following command.
```
docker run -t -i -p 8080:8080 chat-app/vertx-java
```

