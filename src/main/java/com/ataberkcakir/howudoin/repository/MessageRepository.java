package com.ataberkcakir.howudoin.repository;

import com.ataberkcakir.howudoin.model.Message;
import com.ataberkcakir.howudoin.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    // Retrieves messages between a sender and a receiver.
    List<Message> findBySenderAndReceiver(User sender, User receiver);

    // Retrieves messages received by a user.
    List<Message> findByReceiver(User receiver);

    // Retrieves messages sent by a user.
    List<Message> findBySender(User sender);
}
