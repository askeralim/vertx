package io.chatapp.chatroom;

import io.chatapp.chatroom.verticle.ChatRoomVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

@SpringBootApplication
@ComponentScan("io.chatapp")
public class ChatRoomVertxSpringApplication {
    @Autowired
    private ChatRoomVerticle chatRoomVerticle;

    public static void main(String[] args) {
        new SpringApplicationBuilder(ChatRoomVertxSpringApplication.class).web(false).run(args);
    }

    @PostConstruct
    public void deployVerticle() {
        VertxOptions options = new VertxOptions();
        try {
            String dockerIp = InetAddress.getByName("localhost").getHostAddress();//getByName('localhost') getLocalHost()
            options.setClusterHost(dockerIp);
            Vertx.clusteredVertx(options,
                    vertxAsyncResult -> vertxAsyncResult.result().deployVerticle(chatRoomVerticle));
        }catch(Exception e){

        }
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
