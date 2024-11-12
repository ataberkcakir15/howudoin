package com.ataberkcakir.howudoin.service;

import com.ataberkcakir.howudoin.model.Message;
import com.ataberkcakir.howudoin.model.User;
import com.ataberkcakir.howudoin.repository.MessageRepository;
import com.ataberkcakir.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // Send a message from sender to receiver
    public String sendMessage(String senderEmail, String receiverEmail, String content) {
        Optional<User> senderOpt = userRepository.findByEmail(senderEmail);
        Optional<User> receiverOpt = userRepository.findByEmail(receiverEmail);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return "User not found";
        }

        User sender = senderOpt.get();
        User receiver = receiverOpt.get();

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(Instant.now());

        messageRepository.save(message);

        return "Message sent";
    }

    // Retrieve messages between two users
    public List<Message> getMessagesBetweenUsers(String userEmail, String otherUserEmail) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        Optional<User> otherUserOpt = userRepository.findByEmail(otherUserEmail);

        if (userOpt.isEmpty() || otherUserOpt.isEmpty()) {
            return Collections.emptyList();
        }

        User user = userOpt.get();
        User otherUser = otherUserOpt.get();

        List<Message> messages = new ArrayList<>();
        messages.addAll(messageRepository.findBySenderAndReceiver(user, otherUser));
        messages.addAll(messageRepository.findBySenderAndReceiver(otherUser, user));

        // Sort messages by timestamp
        messages.sort(Comparator.comparing(Message::getTimestamp));

        return messages;
    }
}
