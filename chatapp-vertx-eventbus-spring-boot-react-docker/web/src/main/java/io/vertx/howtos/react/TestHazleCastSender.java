package io.vertx.howtos.react;

import com.google.gson.JsonObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.security.Key;
import java.util.Properties;

//import org.json.simple.JSONObject;
class Sender extends AbstractVerticle{
	private int port;
	Sender(int port){
		this.port = port;
	}
	@Override
	public void start() throws Exception {
		Router router = Router.router(vertx);

		// Allow events for the designated addresses in/out of the event bus bridge
//		BridgeOptions opts = new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddress("chat.message"))
//				.addOutboundPermitted(new PermittedOptions().setAddress("chat.message"));

		// Create a router endpoint for the static content.
		router.route().handler(StaticHandler.create());
		router.get("/sendMessage").handler(rc->{
			System.out.println("Received Request for Sending in ======== EventBus =========="+rc.request().getParam("name"));
			vertx.eventBus().publish("chat.message", "My Message Sender :"+rc.request().getParam("name"));
			rc.response().end("Successfully Posted ");
		});

		// Start the web server and tell it to use the router to handle requests.
		vertx.createHttpServer().requestHandler(router).listen(3001);
//		final EventBus eventBus = vertx.eventBus();
//		final String message = routingContext.request().getParam(PATH_PARAM);
//		eventBus.publish("chat.message", message);
//		log.info("Current Thread Id {} Is Clustered {} ", Thread.currentThread().getId(), vertx.isClustered());
//		routingContext.response().end(message);
	}
}
public class TestHazleCastSender extends AbstractVerticle {
	public static void main(String[] args) throws Exception {
		// Vertx vertx = Vertx.vertx(); // <1>
		// vertx.deployVerticle(new BackendVerticle()); // <2>

		String dockerIp = InetAddress.getLocalHost().getHostAddress();//getByName("localhost")
		VertxOptions options = new VertxOptions();
		options.setClusterHost(dockerIp);
		Vertx.clusteredVertx(options,
				vertxAsyncResult -> vertxAsyncResult.result().deployVerticle(new Sender(3002)));//new Receiver()));
	    
	}
}
