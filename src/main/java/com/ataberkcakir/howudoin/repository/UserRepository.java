package com.ataberkcakir.howudoin.repository;

import com.ataberkcakir.howudoin.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email); // for authentication
}
