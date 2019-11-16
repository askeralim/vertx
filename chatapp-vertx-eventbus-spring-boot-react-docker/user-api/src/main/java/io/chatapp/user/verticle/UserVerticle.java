package io.chatapp.user.verticle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.chatapp.dao.model.Status;
import io.chatapp.dao.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.chatapp.dao.app.ApplicationConfiguration;
import io.chatapp.dao.app.AuthenticationService;
import io.chatapp.dao.repository.GroupMemberRepository;
import io.chatapp.dao.repository.UserRepository;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class UserVerticle extends AbstractVerticle {

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        // Allow events for the designated addresses in/out of the event bus bridge
        //Working chat.room
//        BridgeOptions opts = new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddress("chat.room"))
//                .addOutboundPermitted(new PermittedOptions().setAddress("chat.room"));
        //^chat.(room|(\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b)|(room))$
        BridgeOptions opts = new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddressRegex("^chat.(room|message|(\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b)|(room))$"))
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("^chat.(room|message|(\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b)|(room))$"));
//        BridgeOptions opts1 = new BridgeOptions();

        // Create the event bus bridge and add it to the router.
        SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
        router.route("/eventbus/*").handler(ebHandler);
        // Create a router endpoint for the static content.
        router.route().handler(StaticHandler.create());

        // Begin API
        router.post("/user/login").handler(this::login);
        router.post("/user/register").handler(this::register);
        router.get("/user/list").handler(this::userList);
        router.get("/user/data").handler(this::userdata);

        // Start the web server and tell it to use the router to handle requests.
        System.out.println("================ User Server =============== PORT @ -"+applicationConfiguration.getUserApiPort());
        vertx.eventBus().consumer("chat.message", receivedMessage -> {
            System.out.println("Received message: in User Verticle: "+ receivedMessage.body());
        });
        vertx.createHttpServer().requestHandler(router).listen(applicationConfiguration.getUserApiPort());
        System.out.println("Server Started @3001 -"+"  securityKey:"+applicationConfiguration.getSecurityKey()+" Mongo :");

    }

    private void login(RoutingContext rc) {
        System.out.println("Request Received :Login");
        try {
            rc.request().bodyHandler(bodyHandler -> {
                System.out.println("JSON Data :" + bodyHandler.toJsonObject().encodePrettily());
                User user = Json.decodeValue(bodyHandler.toJsonObject().encodePrettily(), User.class);
                System.out.println(user.getUsername());
//                User authenticatedUser = userDao.findUserByUserName(user.getUsername());
                User authenticatedUser = userRepository.findByUsername(user.getUsername());
                JsonObject rootObj = new JsonObject();
                if(authenticatedUser != null && bCryptPasswordEncoder.matches(user.getPassword(),authenticatedUser.getPassword())) {
                    //TODO Implement User Authentication
                    Key signingKey = new SecretKeySpec(applicationConfiguration.getSecurityKey().getBytes(), SignatureAlgorithm.HS256.getJcaName());//props.getProperty("app.security.key")
                    String jwt = Jwts.builder().setSubject("chatapp")
                            .claim("username", authenticatedUser.getUsername())
                            .claim("id", authenticatedUser.getId())
                            .claim("password", java.util.Base64.getUrlEncoder().encodeToString(user.getPassword().getBytes()))
                            .setExpiration(getExpirationDate())
                            .signWith(signingKey).compact();
                    rootObj.addProperty("status", Status.SUCCESS.getStatus());
                    rootObj.addProperty("name", authenticatedUser.getNickName());
                    rootObj.addProperty("username", authenticatedUser.getUsername());
                    rootObj.addProperty("id", authenticatedUser.getId());
                    rootObj.addProperty("token", jwt);
                    rc.response().putHeader("content-type", "application/json; \n" +
                            "                    rootObj.addProperty(\"status\", \"Failed\");charset=utf-8")
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
    private void register(RoutingContext rc) {
        System.out.println("Request Received :Login");
        try {
            rc.request().bodyHandler(bodyHandler -> {
                System.out.println("JSON Data :" + bodyHandler.toJsonObject().encodePrettily());
                User user = Json.decodeValue(bodyHandler.toJsonObject().encodePrettily(), User.class);
                System.out.println(user.getUsername()+" - "+ user.getNickName()+" -"+user.getPassword());

                //TODO Implement User Authentication
                String userId = UUID.randomUUID().toString();
                System.out.println("Before Save:"+user.toString());
//                user = userDao.createUser(new User(userId, user.getNickName(),user.getUsername(),bCryptPasswordEncoder.encode(user.getPassword()), User.Type.USER));
                user = userRepository.save(new User(userId, user.getNickName(),user.getUsername(),bCryptPasswordEncoder.encode(user.getPassword()), User.Type.USER));
                System.out.println("After Save:"+user.toString());
//                userDao.saveUser(user);
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();
                JsonObject rootObj = new JsonObject();
                rootObj.addProperty("status", "Success");
                this.getVertx().eventBus().publish("chat.room", gson.toJson(user));
                rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                        .end(rootObj.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void userList(RoutingContext rc) {
        System.out.println("Request Received : User/List");
        try {
            String token = rc.request().getHeader("token");
            String authenticatedUserId = authenticationService.getAuthenticatedUserId(token);
            if(authenticatedUserId != null) {
                System.out.println("********* USER AUTHENTICATED:**********************:");
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();
                rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                        .end(gson.toJson(userRepository.findAll()).toString());
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
    private Date getExpirationDate() {
        long ttlMillis = 24 * 3600000;
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + ttlMillis;
        return new Date(expMillis);
    }

    private void userdata(RoutingContext rc) {
        System.out.println("Request Received :User Data");
        try {
                String userId = UUID.randomUUID().toString();
                User group = new User(userId, "My Chat Room", "", "", User.Type.CHAT_ROOM);
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();
                this.getVertx().eventBus().publish("chat.room", gson.toJson(group));
                this.getVertx().eventBus().send("chat.9cee7e14-7134-4151-acba-1646f222f949", gson.toJson(group));

                rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(Status.SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Saving Object ERROR");
            rc.response().setStatusCode(403).putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(Status.FAIL));
        }
    }
}