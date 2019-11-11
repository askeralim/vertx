package io.chatapp.dao.repository;

import io.chatapp.dao.DaoSpringApplication;
import io.chatapp.dao.model.GroupMemberPrimaryKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import io.chatapp.dao.model.GroupMember;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaoSpringApplication.class)
public class GroupMemberRepositoryTest {

    @Autowired
    private GroupMemberRepository repository;

    @Before
    public void init() {
        repository.deleteAll();
    }

    @After
    public void finalize() {
        repository.deleteAll();
    }

    @Test
    public void shouldSaveGroupMember() throws Exception {
        String userId = UUID.randomUUID().toString();
        GroupMemberPrimaryKey key = new GroupMemberPrimaryKey("askerali","group10");
        GroupMember member = new GroupMember(key,new Date());

        repository.save(member);

        GroupMember actualUser = repository.findOne(key);
        assertThat(actualUser.getId().getGroupId()).isEqualTo("group10");
    }

    @Test
    public void shouldReturnAllUsers() throws Exception {
        String userId = UUID.randomUUID().toString();
        GroupMemberPrimaryKey key = new GroupMemberPrimaryKey("askerali","group1");
        GroupMember member = new GroupMember(key,new Date());
        repository.save(member);
        userId = UUID.randomUUID().toString();
        key = new GroupMemberPrimaryKey("Haris","group1");
        member = new GroupMember(key,new Date());
        repository.save(member);

        List<GroupMember> memberList = repository.findByGroupId("group1");
        System.out.println(memberList.size());
        assertThat(memberList.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnAllGroupsOfUser() throws Exception {
        String userId = UUID.randomUUID().toString();
        GroupMemberPrimaryKey key = new GroupMemberPrimaryKey("askerali","group1");
        GroupMember member = new GroupMember(key,new Date());
        repository.save(member);

        userId = UUID.randomUUID().toString();
        key = new GroupMemberPrimaryKey("Haris","group1");
        member = new GroupMember(key,new Date());
        repository.save(member);

        List<GroupMember> memberList = repository.findByUserId("askerali");
        System.out.println(memberList.size());
        assertThat(memberList.size()).isEqualTo(1);
    }
}