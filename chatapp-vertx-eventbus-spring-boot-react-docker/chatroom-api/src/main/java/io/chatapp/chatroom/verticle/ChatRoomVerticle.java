package io.chatapp.chatroom.verticle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.chatapp.dao.model.GroupMember;
import io.chatapp.dao.model.GroupMemberPrimaryKey;
import io.chatapp.dao.model.Status;
import io.chatapp.dao.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.chatapp.dao.app.ApplicationConfiguration;
import io.chatapp.dao.app.AuthenticationService;
import io.chatapp.dao.repository.GroupMemberRepository;
import io.chatapp.dao.repository.UserRepository;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Component
public class ChatRoomVerticle extends AbstractVerticle {

    @Autowired
    ApplicationConfiguration configuration;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);
        // Create a router endpoint for the static content.
        router.route().handler(StaticHandler.create());

        // Begin API
        router.post("/chatroom/create").handler(this::createChatRoom);
        router.post("/chatroom/join").handler(this::joinChatRoom);
        router.get("/chatroom/list").handler(this::userChatRoomList);

        // Start the web server and tell it to use the router to handle requests.
        System.out.println("================ Chatroom Server =============== PORT @ -"+configuration.getChatroomApiPort());
        vertx.createHttpServer().requestHandler(router).listen(configuration.getChatroomApiPort());
        System.out.println("Server Started @ -"+configuration.getChatroomApiPort()+"  securityKey:"+configuration.getSecurityKey()+" Mongo :");

    }

    private void createChatRoom(RoutingContext rc) {
        System.out.println("Request Received :Login");
        try {
            rc.request().bodyHandler(bodyHandler -> {
                System.out.println("JSON Data :" + bodyHandler.toJsonObject().encodePrettily());
                User user = Json.decodeValue(bodyHandler.toJsonObject().encodePrettily(), User.class);
                String authenticatedUserId = authenticationService.getAuthenticatedUserId(user.getToken());
                if(authenticatedUserId != null) {
                    System.out.println("Group Name :"+user.getNickName());
                    String userId = UUID.randomUUID().toString();
                    User group = new User(userId, user.getNickName(),"","", User.Type.CHAT_ROOM);
                    Gson gson = new GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create();
//                    userDao.createUser(group);
                    userRepository.save(group);
                    JsonObject rootObj = new JsonObject();
                    rootObj.addProperty("status", "Success");
                    rootObj.addProperty("group", gson.toJson(group));
                    //Publishing to Event Bus
                    this.getVertx().eventBus().publish("chat.room", gson.toJson(group));
                    rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                            .end(rootObj.toString());
                }else{
                    rc.response().setStatusCode(403).putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(Status.FAIL));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void joinChatRoom(RoutingContext rc) {
        System.out.println("Request Received :Join ChatRoom");
        try {
            rc.request().bodyHandler(bodyHandler -> {
                System.out.println("JSON Data :" + bodyHandler.toJsonObject().encodePrettily());
                GroupMemberPrimaryKey key= Json.decodeValue(bodyHandler.toJsonObject().encodePrettily(), GroupMemberPrimaryKey.class);
                String authenticatedUserId = authenticationService.getAuthenticatedUserId(key.getToken());
                if(authenticatedUserId != null) {
                    System.out.println("Group Name :"+key.getGroupId());
                    key.setUserId(authenticatedUserId);
                    GroupMember member = new GroupMember(key, new Date());
                    groupMemberRepository.save(member);
                    JsonObject rootObj = new JsonObject();
                    rootObj.addProperty("status", "Success");
                    rootObj.addProperty("groupId", key.getGroupId());
                    rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(rootObj.toString()));
                }else{
                    rc.response().setStatusCode(403).putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(Status.FAIL));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userChatRoomList(RoutingContext rc) {
        System.out.println("Request Received :");
        try {
            String token = rc.request().getHeader("token");
            String authenticatedUserId = authenticationService.getAuthenticatedUserId(token);
            if(authenticatedUserId != null) {
                System.out.println("********* USER AUTHENTICATED:**********************:"+authenticatedUserId);
                List<GroupMember> memberList = groupMemberRepository.findByUserId(authenticatedUserId);
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();
                System.out.println(memberList.size()+"JSON :"+gson.toJson(memberList).toString());
                rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                        .end(gson.toJson(memberList).toString());
                System.out.println("Sending in Event Bus:******** END");
                return;
            }else{
                System.out.println("********* INVALID AUTHENTICATED:**********************:");
                rc.response().setStatusCode(403).putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(Status.FAIL));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void data(RoutingContext rc) {
        System.out.println("Request Received :User Data");
        try {
            rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(Status.SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Saving Object ERROR");
            rc.response().setStatusCode(403).putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(Status.FAIL));
        }
    }
//    private String getAuthenticatedUserId(String token){
//        String authenticatedUserId = null;
//        try{
//            authenticatedUserId = Jwts.parser().setSigningKey(new SecretKeySpec(configuration.getSecurityKey().getBytes(), SignatureAlgorithm.HS256.getJcaName())).parseClaimsJws(token).getBody().get("id").toString();
//        }catch(Exception e){
//
//        }
//        return authenticatedUserId;
//    }
}