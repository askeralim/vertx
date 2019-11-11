package io.chatapp.dao.model;


import com.google.gson.annotations.Expose;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "groupmembers")
public class GroupMember {

    @Id
    @Expose
    private GroupMemberPrimaryKey id;
    private Date joiningDate;

    public GroupMember(GroupMemberPrimaryKey id, Date joiningDate) {
        this.id = id;
        this.joiningDate = joiningDate;
    }

    public GroupMemberPrimaryKey getId() {
        return id;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }
}
