package io.vertx.howtos.react;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.security.Key;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;

//import org.json.simple.JSONObject;
import com.google.gson.JsonObject;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class BackendVerticle extends AbstractVerticle {
	private Properties props = null;
//	public BackendVerticle() {
//		// Properties props = new Properties();
////		props = new Properties();
////		try {
//////			props.load(BackendVerticle.class.getClassLoader().getResourceAsStream("security.properties"));
//////			System.out.println("Constructor props :"+props.getProperty("app.security.key"));
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	}
	@Override
	public void start() throws Exception {

		Router router = Router.router(vertx);
//
//		// Allow events for the designated addresses in/out of the event bus bridge
//		BridgeOptions opts = new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddress("chat.message"))
//				.addOutboundPermitted(new PermittedOptions().setAddress("chat.message"));

		// Create the event bus bridge and add it to the router.
//		SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
//		router.route("/eventbus/*").handler(ebHandler);
		// ebHandler.

		// Create a router endpoint for the static content.
		router.route().handler(StaticHandler.create());

		// Begin API
//		router.post("/api/message").handler(this::postMessage);
//		router.post("/user/login").handler(this::login);
//		router.get("/user/data").handler(this::userdata);

		// Start the web server and tell it to use the router to handle requests.
		vertx.createHttpServer().requestHandler(router).listen(3001);
		System.out.println("Server Started @3001");
	}

	private void postMessage(RoutingContext rc) {
		System.out.println("Request Received :");
		try {
			rc.request().bodyHandler(bodyHandler -> {
				System.out.println("JSON Data :" + bodyHandler.toJsonObject().encodePrettily());
				Message msg = Json.decodeValue(bodyHandler.toJsonObject().encodePrettily(), Message.class);
				System.out.println(msg.getAuthor());
				JsonObject obj = new JsonObject();
				obj.addProperty("author", msg.getAuthor());
				obj.addProperty("message", msg.getMessage());
				System.out.println("Sending in Event Bus:**********************:" + obj.toString() + "\n"
						+ obj.toString().replaceAll("\"", "'"));
				vertx.eventBus().send("chat.message", obj.toString().replaceAll("\"", "'"));// JSON.encode(msg)
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(Status.SUCCESS));
	}

	private void login(RoutingContext rc) {
		System.out.println("Request Received :Login");
		try {

			rc.request().bodyHandler(bodyHandler -> {
				System.out.println("JSON Data :" + bodyHandler.toJsonObject().encodePrettily());
				User user = Json.decodeValue(bodyHandler.toJsonObject().encodePrettily(), User.class);
				System.out.println(user.getUsername());
				Key signingKey = new SecretKeySpec(props.getProperty("app.security.key").getBytes(), SignatureAlgorithm.HS256.getJcaName());
			    
				String jwt = Jwts.builder().setSubject("Joe").claim("username", "Asker")
						.claim("password", java.util.Base64.getUrlEncoder().encodeToString("password".getBytes()))
						.signWith(signingKey).compact();
				
				JsonObject rootObj = new JsonObject();
				rootObj.addProperty("status","Success");
				rootObj.addProperty("name","Asker Ali M");
				rootObj.addProperty("username",user.getUsername());
				rootObj.addProperty("token",jwt);
				rc.response().putHeader("content-type", "application/json; charset=utf-8")
						.end(rootObj.toString());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		// rc.response().setStatusCode(200).putHeader("content-type", "application/json;
		// charset=utf-8")
		// .end(Json.encodePrettily(Status.SUCCESS));
	}

	private InputStream getFileFromResources(String fileName) throws Exception{

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new FileInputStream(new File(resource.getFile()));
        }

    }

	private void userdata(RoutingContext rc) {
		System.out.println("Request Received :Data");
		try {
			User u = new User();
			u.setPassword("Password");
			u.setUsername("Asker");
			rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(u));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// rc.response().setStatusCode(200).putHeader("content-type", "application/json;
		// charset=utf-8")
		// .end(Json.encodePrettily(Status.SUCCESS));
	}

	// tag::main[]
	public static void main(String[] args) throws Exception {
		 Vertx vertx = Vertx.vertx(); // <1>
		 vertx.deployVerticle(new BackendVerticle()); // <2>

//		String dockerIp = InetAddress.getByName("localhost").getHostAddress();
//		VertxOptions options = new VertxOptions();
//		options.setClusterHost(dockerIp);
//		Vertx.clusteredVertx(options,
//				vertxAsyncResult -> vertxAsyncResult.result().deployVerticle(new BackendVerticle()));
	    
		
//		Properties props = new Properties();
//		props.load(new BackendVerticle().getFileFromResources("security.properties"));
//	    MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] digest = md.digest("MySecret".getBytes(StandardCharsets.UTF_8));
//        String sha256 = DatatypeConverter.printHexBinary(digest).toLowerCase();
//        String keyVal = props.getProperty("app.security.key");
//        System.out.println(keyVal);
        
//        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("MySecret");
//	    Key signingKey = new SecretKeySpec(keyVal.getBytes(), SignatureAlgorithm.HS256.getJcaName());
//	    
//		String jwt = Jwts.builder().setSubject("Joe").claim("username", "Asker")
//				.claim("password", java.util.Base64.getUrlEncoder().encodeToString("password".getBytes()))
//				.signWith(signingKey).compact();
//		System.out.println("JWT:" + jwt);
//		String s="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKb2UiLCJ1c2VybmFtZSI6IkFza2VyIiwicGFzc3dvcmQiOiJjR0Z6YzNkdmNtUT0ifQ.78Mk33X16LtXY_3XOjXjJasmh1PC0Ptlsc-CA-cK6jvKcy4hXTjCslBK5J-rqObpvRfFFBwvJmJY0mifsi6r0Q";
////		String keyVal="49562cfc3b17139ea01c480b9c86a2ddacb38ff1b2e9db1bf66bab7a4e3f1fb5";
//		String userName =  Jwts.parser().setSigningKey(new SecretKeySpec(keyVal.getBytes(), SignatureAlgorithm.HS256.getJcaName())).parseClaimsJws(s).getBody().get("username").toString();
//		System.out.println(userName);
		
//		JsonObject rootObj = new JsonObject();
//		rootObj.addProperty("status","Success");
//		rootObj.addProperty("name","Asker Ali M");
//		rootObj.addProperty("username","askerlim");
//		rootObj.addProperty("token","token");
//		System.out.println(rootObj.toString()+props.getProperty("app.security.key"));

	}
	// end::main[]
}
