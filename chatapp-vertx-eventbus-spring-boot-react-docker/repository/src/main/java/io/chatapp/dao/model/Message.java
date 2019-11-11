package io.chatapp.dao.model;

import com.google.gson.annotations.Expose;

public class Message extends Token{
    @Expose
    private String id;
    @Expose
    private String fromId;
    @Expose
    private String toId;
    @Expose
    private String message;
    @Expose
    private User.Type recepientType;
    @Expose
    private String fromName;
    @Expose
    private String toName;

    public Message(String id, String fromId, String toId, String message, User.Type recepientType, String fromName, String toName) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.message = message;
        this.recepientType = recepientType;
        this.fromName = fromName;
        this.toName = toName;
    }
    public Message(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public User.Type getRecepientType() {
        return recepientType;
    }

    public void setRecepientType(User.Type recepientType) {
        this.recepientType = recepientType;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("from='").append(fromId);
        sb.append("', to='").append(toId).append('\'');
        sb.append("', from='").append(fromName).append('\'');
        sb.append("', to='").append(toName).append('\'');
        sb.append("', message='").append(message).append('\'');
        sb.append(super.toString()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
