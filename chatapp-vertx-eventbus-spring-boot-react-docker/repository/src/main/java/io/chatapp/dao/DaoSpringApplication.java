package io.chatapp.dao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@ComponentScan("io.chatapp")
public class DaoSpringApplication {
//    @Autowired
//    private UserVerticle userVerticle;

    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println("Matched :"+encoder.matches("password",encoder.encode("password")));
        SpringApplication.run(DaoSpringApplication.class, args);
    }

//    @PostConstruct
//    public void deployVerticle() {
//        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(userVerticle);
//    }
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
