package io.chatapp.dao.model;

import com.google.gson.annotations.Expose;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User extends Token{
    public enum Status {
        ONLINE,
        OFFLINE
    }
    public enum Type {
        USER,
        CHAT_ROOM
    }
    @Id
    @Expose
    private String id;
    @Expose
    private String nickName;
    @Expose
    private String username;
    @Expose
    private Status status = Status.OFFLINE;
    @Expose
    private Type type = Type.USER;
    private String password;

    public User(){

    }
    public User(String id, String nickName, String username, String password, Type type){
        this.id=id;
        this.nickName = nickName;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id+" - "+nickName+" - "+username+"- "+password;
    }
}
