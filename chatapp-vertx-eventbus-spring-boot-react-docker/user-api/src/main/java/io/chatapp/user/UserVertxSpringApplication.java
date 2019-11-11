package io.chatapp.user;

import io.chatapp.user.verticle.UserVerticle;
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
public class UserVertxSpringApplication {
    @Autowired
    private UserVerticle userVerticle;

    public static void main(String[] args) {
        new SpringApplicationBuilder(UserVertxSpringApplication.class).web(false).run(args);
    }

    @PostConstruct
    public void deployVerticle() {
        try {
            String dockerIp = InetAddress.getByName("localhost").getHostAddress();
            VertxOptions options = new VertxOptions();
//            ClusterManager mgr = new HazelcastClusterManager();
//            options.setClusterManager(mgr);//lusterHost(dockerIp);
            options.setClusterHost(dockerIp);
            Vertx.clusteredVertx(options,
                    vertxAsyncResult -> vertxAsyncResult.result().deployVerticle(userVerticle));
        }catch(Exception e){

        }
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
