package io.chatapp.dao.repository;

import io.chatapp.dao.DaoSpringApplication;
import io.chatapp.dao.model.Message;
import io.chatapp.dao.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaoSpringApplication.class)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository repository;
    @Before
    public void init() {
        repository.deleteAll();
    }

    @After
    public void finalize() {
        repository.deleteAll();
    }
    @Test
    public void shouldSaveTheUser() throws Exception {
        String id = UUID.randomUUID().toString();
        //public Message(String id, String fromId, String toId, String message, User.Type recepientType, String fromName, String toName)
        Message message= new Message(id,"Asker","Haris","Hi Haris", User.Type.USER,"Asker", "Haris");

        repository.save(message);

        List<Message> messageList = repository.findMessageForUserId("Haris");
        assertThat(messageList.size()).isEqualTo(1);
        assertThat(messageList.get(0).getId()).isEqualTo(id);
        assertThat(messageList.get(0).getToId()).isEqualTo("Haris");
    }

    @Test
    public void shouldReturnAllMesages() throws Exception {
        String id = UUID.randomUUID().toString();
        Message message= new Message(id,"Asker","Haris","Hi Haris",User.Type.USER,"Asker", "Haris");
        repository.save(message);
        message= new Message(id,"Habeeb","Haris","Hi Haris",User.Type.USER,"Habeeb", "Haris");
        repository.save(message);
        List<Message> messageList = repository.findMessageForUserId("Haris");
        assertThat(messageList.size()).isEqualTo(1);
    }
}