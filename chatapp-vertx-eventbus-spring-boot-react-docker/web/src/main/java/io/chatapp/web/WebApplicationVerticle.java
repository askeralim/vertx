package io.chatapp.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class WebApplicationVerticle extends AbstractVerticle {
	@Override
	public void start() throws Exception {

		Router router = Router.router(vertx);
		router.route().handler(StaticHandler.create());
		// Start the web server and tell it to use the router to
		// handle requests.
		vertx.createHttpServer().requestHandler(router).listen(3000);
		System.out.println("Server Started @3000");
	}
	public static void main(String[] args) throws Exception {
		 Vertx vertx = Vertx.vertx(); // <1>
		 vertx.deployVerticle(new WebApplicationVerticle()); // <2>
	}
}
