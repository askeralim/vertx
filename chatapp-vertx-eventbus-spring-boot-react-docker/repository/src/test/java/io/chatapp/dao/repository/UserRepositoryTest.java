package io.chatapp.dao.repository;

import io.chatapp.dao.DaoSpringApplication;
import io.chatapp.dao.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaoSpringApplication.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
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
        String userId = UUID.randomUUID().toString();
        User user = new User(userId,"Asker","","",User.Type.USER);

        repository.save(user);

        User actualUser = repository.findOne(userId);
        assertThat(actualUser.getId()).isEqualTo(userId);
    }

    @Test
    public void shouldReturnAllUsers() throws Exception {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId,"Asker","","",User.Type.USER);

        repository.save(user);
    }
}