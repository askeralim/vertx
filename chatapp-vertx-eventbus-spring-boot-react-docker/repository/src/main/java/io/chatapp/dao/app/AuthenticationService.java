package io.chatapp.dao.app;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;

@Service
public class AuthenticationService {
    @Autowired
    ApplicationConfiguration configuration;

    public String getAuthenticatedUserId(String token){
        String authenticatedUserId = null;
        try{
            authenticatedUserId = Jwts.parser().setSigningKey(new SecretKeySpec(configuration.getSecurityKey().getBytes(), SignatureAlgorithm.HS256.getJcaName())).parseClaimsJws(token).getBody().get("id").toString();
        }catch(Exception e){

        }
        return authenticatedUserId;
    }
}
