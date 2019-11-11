package io.chatapp.dao.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class GroupMemberPrimaryKey extends Token implements Serializable {
    @Expose
    private String userId;
    @Expose
    private String groupId;

    public GroupMemberPrimaryKey(){
    }
    public GroupMemberPrimaryKey(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
