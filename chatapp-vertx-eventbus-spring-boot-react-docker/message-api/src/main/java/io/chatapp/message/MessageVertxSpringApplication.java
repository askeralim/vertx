package io.chatapp.message;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.chatapp.message.verticle.MessageVerticle;
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
public class MessageVertxSpringApplication {
    @Autowired
    private MessageVerticle messageVerticle;

    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println("Matched :"+encoder.matches("password",encoder.encode("password")));
//        SpringApplication.run(MessageVertxSpringApplication.class, args);
        new SpringApplicationBuilder(MessageVertxSpringApplication.class).web(false).run(args);
    }

    @PostConstruct
    public void deployVerticle() {
//        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(messageVerticle);
        VertxOptions options = new VertxOptions();
//        ClusterManager mgr = new HazelcastClusterManager();
//        options.setClusterManager(mgr);//lusterHost(dockerIp);
        try {
            String dockerIp = InetAddress.getByName("localhost").getHostAddress();
            options.setClusterHost(dockerIp);
            Vertx.clusteredVertx(options,
                    vertxAsyncResult -> vertxAsyncResult.result().deployVerticle(messageVerticle));
        }catch(Exception e){

        }
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    public @Bean
//    MongoTemplate mongoTemplate() throws Exception {
//        org.springframework.data.authentication.UserCredentials
//        MongoTemplate mongoTemplate =
//                new MongoTemplate(new MongoClient("127.0.0.1"),"chatapp");
//        return mongoTemplate;
//
//    }
}
