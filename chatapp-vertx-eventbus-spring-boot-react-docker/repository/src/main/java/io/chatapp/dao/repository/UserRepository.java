package io.chatapp.dao.repository;

import io.chatapp.dao.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ 'username' : ?0 }")
    User findByUsername(String username);

}
