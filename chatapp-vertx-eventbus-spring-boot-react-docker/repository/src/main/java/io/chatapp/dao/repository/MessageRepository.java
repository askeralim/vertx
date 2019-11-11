package io.chatapp.dao.repository;

import io.chatapp.dao.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    @Query("{ 'toId' : ?0 }")
    List<Message> findMessageForUserId(String id);

}
