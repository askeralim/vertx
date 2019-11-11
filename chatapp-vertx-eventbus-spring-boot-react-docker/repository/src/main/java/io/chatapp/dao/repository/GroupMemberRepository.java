package io.chatapp.dao.repository;

import io.chatapp.dao.model.GroupMember;
import io.chatapp.dao.model.GroupMemberPrimaryKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends MongoRepository<GroupMember, GroupMemberPrimaryKey> {
    @Query("{ 'id.groupId' : ?0 }")
    List<GroupMember> findByGroupId(String groupId);
    @Query("{ 'id.userId' : ?0 }")
    List<GroupMember> findByUserId(String userId);
}
