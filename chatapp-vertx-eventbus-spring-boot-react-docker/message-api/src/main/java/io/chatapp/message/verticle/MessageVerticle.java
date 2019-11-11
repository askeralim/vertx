package io.chatapp.message.verticle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.chatapp.dao.model.Message;
import io.chatapp.dao.model.Status;
import io.chatapp.dao.repository.MessageRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.chatapp.dao.app.ApplicationConfiguration;
import io.chatapp.dao.app.AuthenticationService;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class MessageVerticle extends AbstractVerticle {

    @Autowired
    ApplicationConfiguration configuration;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);
        // Create a router endpoint for the static content.
        router.route().handler(StaticHandler.create());

        // Begin API
        router.post("/message").handler(this::postMessage);
        router.get("/message/data").handler(this::data);

        // Start the web server and tell it to use the router to handle requests.
        System.out.println("================ Message Server =============== PORT @ -"+configuration.getMessageApiPort());
        vertx.createHttpServer().requestHandler(router).listen(configuration.getMessageApiPort());
        System.out.println("Server Started @ -"+configuration.getMessageApiPort()+"  securityKey:"+configuration.getSecurityKey()+" Mongo :");

    }

    private void postMessage(RoutingContext rc) {
        System.out.println("Request Received :");
        try {
            rc.request().bodyHandler(bodyHandler -> {
                System.out.println("JSON Data :" + bodyHandler.toJsonObject().encodePrettily());
                Message msg = Json.decodeValue(bodyHandler.toJsonObject().encodePrettily(), Message.class);
                System.out.println(msg.toString());
                msg.setId(UUID.randomUUID().toString());
                String authenticatedUserId = authenticationService.getAuthenticatedUserId(msg.getToken());
                if(authenticatedUserId != null) {
                    System.out.println("********* USER AUTHENTICATED:**********************:");
                    Gson gson = new GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create();
                    System.out.println("Sending in Event Bus:**********************:" + gson.toJson(msg).toString());
                    messageRepository.save(msg);
                    vertx.eventBus().publish("chat.message", gson.toJson(msg).toString());
                    //TODO Implement individual message sending, which is failing now.
//                    if(msg.getRecepientType() == User.Type.CHAT_ROOM){
//                        System.out.println("Sending in Chat Room :**********************:To:"+msg.getToName()+"   From:"+msg.getFromName());
//                        List<GroupMember> roomMembers = groupMemberRepository.findByGroupId(msg.getToId());
//                        //roomMembers.stream().forEach(user->vertx.eventBus().send("chat."+user.getId(), gson.toJson(msg).toString()));
//                        //vertx.eventBus().publish("chat."+msg.getToId(), gson.toJson(msg).toString());
//                    }else {
//                        System.out.println("Sending in Person:**********************:To:"+msg.getToName()+"   From:"+msg.getFromName());
//                        vertx.eventBus().send("chat." + msg.getFromId(), gson.toJson(msg).toString());// JSON.encode(msg)
//                        vertx.eventBus().send("chat." + msg.getToId(), gson.toJson(msg).toString());// JSON.encode(msg)
//                    }
                    rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(Status.SUCCESS));
                    System.out.println("Sending in Event Bus:******** END");
                    return;
                }else{
                    System.out.println("********* INVALID AUTHENTICATED:**********************:");
                    rc.response().setStatusCode(403).putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(Status.FAIL));
                    return;
                }
            });
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