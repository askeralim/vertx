package io.vertx.howtos.react;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.gson.JsonObject;

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

	//// @Override
	// public void start1() throws Exception {
	// // tag::backend[]
	// Router router = Router.router(vertx);
	// Route messageRoute = router.get("/api/message"); // <1>
	// messageRoute.handler(rc -> {
	// System.out.println("Got Request");
	// rc.response().end("Hello React from Vert.x! Test Here"); // <2>
	//
	// });
	//
	// router.get().handler(StaticHandler.create()); // <3>
	//
	// vertx.createHttpServer()
	// .requestHandler(router)
	// .listen(8080);
	//
	// System.out.println("Started Listening @8080");
	//
	// // end::backend[]
	// }

	@Override
	public void start() throws Exception {

		Router router = Router.router(vertx);

		// Allow events for the designated addresses in/out of the event bus bridge
		BridgeOptions opts = new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddress("chat.message"))
				.addOutboundPermitted(new PermittedOptions().setAddress("chat.message"));

		// Create the event bus bridge and add it to the router.
		SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
		router.route("/eventbus/*").handler(ebHandler);
		// ebHandler.

		// Create a router endpoint for the static content.
		router.route().handler(StaticHandler.create());
		
		// Begin API
		router.post("/api/message").handler(this::postMessage);

		// Start the web server and tell it to use the router to handle requests.
		vertx.createHttpServer().requestHandler(router).listen(8080);
		System.out.println("Server Started @8080");
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
				System.out.println("Sending in Event Bus:**********************:" +obj.toString()+"\n"+ obj.toString().replaceAll("\"", "'"));
				vertx.eventBus().send("chat.message", obj.toString().replaceAll("\"", "'"));//JSON.encode(msg)
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		rc.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(Status.SUCCESS));
	}

	// tag::main[]
	public static void main(String[] args) throws UnknownHostException {
		// Vertx vertx = Vertx.vertx(); // <1>
		// vertx.deployVerticle(new BackendVerticle()); // <2>

		String dockerIp = InetAddress.getByName("localhost").getHostAddress();
		VertxOptions options = new VertxOptions();
		options.setClusterHost(dockerIp);
		Vertx.clusteredVertx(options,
				vertxAsyncResult -> vertxAsyncResult.result().deployVerticle(new BackendVerticle()));
	}
	// end::main[]
}
