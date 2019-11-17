# ChatApp - Using Vert.x EventBus, React.js & Mongo DB - Built with SpringBoot
### Background
As part of understanding the concept of Vert.x eventbus & node.js with docker I have combined same existing ChatApplication application with Vert.x, I hope it will help you to do a quick start on your learning journey of Vert.x and SpringBoot

### GIT Link
```
git clone https://github.com/askeralim/vertx.git
```
### Projects
### Project 1: ChatApp  
A Complete Vert.x+React.js with EVentBus ChatApplication developed, backend run by SpringBoot + Vert.x Verticles with Mongo DB.

### Steps to run the application using docker-compose
#### Get the project
```
git clone https://github.com/askeralim/vertx.git
```
After cloning the project change to the project directory
```
cd chatapp-vertx-eventbus-spring-boot-react-docker
mvn clean install
```
The above command will test and build the application, To test the application it require a MongoDB, Mongo DB Configuration can be updated in application.properties 

Link to application.properties :
```
https://github.com/askeralim/vertx/blob/master/chatapp-vertx-eventbus-spring-boot-react-docker/repository/src/main/resources/application.properties
```
##### Command to install MongoDB in Ubuntu
```
sudo apt update
sudo apt install mongodb      # Install Mongo DB
sudo systemctl status mongodb # Start the service
```
##### Command to Create user in Mongo DB
```
db.createUser({user:"admin", pwd:"password", roles:[{role:"root", db:"admin"}]})
```
##### Command to Stop MongoDB service
```
sudo service mongodb stop
```
#### Command Build the Docker Images
The following command will build all the required docker images.
```
docker-compose build
```
### ChatApp docker-compose file
```
version: '3'

services:
  ################################
  #   Setup MongoDB
  ################################
  mongodb:
    image: mongo
    restart: always
    environment:
      MONGO_INITDB_DATABASE: chatapp
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: password
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data
  ################################
  #   Setup web ReactApp vertex
  ################################
  web:
    build:
      context: .
      dockerfile: ./web/Dockerfile
    image: chatapp/web
    depends_on:
      - chatroom-api
      - user-api
      - message-api
    expose:
      - 3000
  ################################
  #   Setup chatroom-api vertex
  ################################
  chatroom-api:
    build:
      context: .
      dockerfile: ./chatroom-api/Dockerfile
    image: chatapp/chatroom-api
    depends_on:
      - mongodb
    expose:
      - 3000
  ################################
  #   Setup user-api vertex
  ################################
  user-api:
    build:
      context: .
      dockerfile: ./user-api/Dockerfile
    image: chatapp/user-api
    depends_on:
      - mongodb
    expose:
      - 3000
  ################################
  #   Setup message-api vertex
  ################################
  message-api:
    build:
      context: .
      dockerfile: ./message-api/Dockerfile
    image: chatapp/message-api
    depends_on:
      - mongodb
    expose:
      - 3000

####################################################
#   Setup nginx load balancer & Reverse proxy
####################################################
  nginx:
    build: ./nginx
    image: chatapp/nginx
    #image: nginx:1.13 # this will use the latest version of 1.13.x
    ports:
      - '80:80' # expose 80 on host and sent to 80 in container
    depends_on: 
      - web
    # volumes:
    #   - ./nginx.conf:/etc/nginx/conf.d/default.conf:ro
    #   - ./client/img:/etc/nginx/html/img
volumes:
  mongodb_data:
```
### NGINX configuration as follows
```
map $http_upgrade $connection_upgrade {
	default upgrade;
	'' close;
}
upstream web_LB {
	server web:3000 weight=3;
}
upstream user_LB {
	server user-api:3000;
}
upstream message_LB {
	server message-api:3000;
}
upstream chatroom_LB {
	server chatroom-api:3000;
}
upstream eventbus_LB {
	server user-api:3000;
}
server {
	listen 80;
	server_name www.chatapp.com;
	location / {
		proxy_pass http://web_LB;
	}
	location /user/ {
		proxy_pass http://user_LB;
	}
	location /message {
		proxy_pass http://message_LB;
	}
	location /chatroom/ {
		proxy_pass http://chatroom_LB;
	}
	location /eventbus/ {
		proxy_pass http://user_LB;
			proxy_http_version 1.1;
		proxy_set_header Upgrade $http_upgrade;
		proxy_set_header Connection $connection_upgrade;
	}
	location /sockjs-node/ {
		proxy_pass http://web_LB;
			proxy_http_version 1.1;
		proxy_set_header Upgrade $http_upgrade;
		proxy_set_header Connection $connection_upgrade;
	}
}

```
### Command to run the ChatApp using docker-compose
```
docker-compose up
```
### Running Application in Browser:
The application can be accessed with link, The nginx is configured to access the React application port 80,
```
http://localhost
```

### Find out more about me

Askerali Maruthullathil [ - LinkedIn](http://linkedin.com/in/askeralim) 
