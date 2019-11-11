package io.chatapp.chatroom;

import io.chatapp.dao.DaoSpringApplication;
import io.chatapp.dao.app.ApplicationConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaoSpringApplication.class)
public class ChatRoomSpringApplicationTests {

	@Autowired
	ApplicationConfiguration configuration;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testProperties(){
		System.out.println("ASKER:"+configuration.getChatroomApiPort());
		assertThat(configuration.getChatroomApiPort()).isEqualTo(3003);
	}
}
