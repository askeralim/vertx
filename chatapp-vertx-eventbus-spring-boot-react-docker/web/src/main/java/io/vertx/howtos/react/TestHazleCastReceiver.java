package io.vertx.howtos.react;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import java.net.InetAddress;

//import org.json.simple.JSONObject;

class Receiver extends AbstractVerticle{
	@Override
	public void start() throws Exception {
		final EventBus eventBus = vertx.eventBus();
		eventBus.consumer("chat.message", receivedMessage -> {
			System.out.println("Received message: {} "+ receivedMessage.body());
		});
	}
}

public class TestHazleCastReceiver extends AbstractVerticle {
	public static void main(String[] args) throws Exception {
		// Vertx vertx = Vertx.vertx(); // <1>
		// vertx.deployVerticle(new BackendVerticle()); // <2>

		String dockerIp = InetAddress.getLocalHost().getHostAddress();//getByName("localhost")
		VertxOptions options = new VertxOptions();
		options.setClusterHost(dockerIp);
		Vertx.clusteredVertx(options,
				vertxAsyncResult -> vertxAsyncResult.result().deployVerticle(new Receiver()));//new Receiver()));
	    
	}
}
