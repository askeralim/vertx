package io.chatapp.dao.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Configuration
@Component
@ConfigurationProperties
public class ApplicationConfiguration {
    @Value( "${user.api.port}" )
    private Integer userApiPort;

    @Value( "${message.api.port}" )
    private Integer messageApiPort;

    @Value( "${chatroom.api.port}" )
    private Integer chatroomApiPort;

    @Value( "${app.security.key}" )
    private String securityKey;

    public Integer getUserApiPort() {
        return userApiPort;
    }

    public Integer getMessageApiPort() {
        return messageApiPort;
    }

    public Integer getChatroomApiPort() {
        return chatroomApiPort;
    }

    public String getSecurityKey() {
        return securityKey;
    }
}
