package com.ataberkcakir.howudoin.service;

import com.ataberkcakir.howudoin.model.Group;
import com.ataberkcakir.howudoin.model.GroupMessage;
import com.ataberkcakir.howudoin.model.User;
import com.ataberkcakir.howudoin.repository.GroupMessageRepository;
import com.ataberkcakir.howudoin.repository.GroupRepository;
import com.ataberkcakir.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        GroupMessageRepository groupMessageRepository,
                        UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMessageRepository = groupMessageRepository;
        this.userRepository = userRepository;
    }

    // Create a new group
    public Group createGroup(String creatorEmail, String groupName, Set<String> memberEmails) {
        Optional<User> creatorOpt = userRepository.findByEmail(creatorEmail);
        if (creatorOpt.isEmpty()) {
            throw new IllegalArgumentException("Creator not found");
        }
        User creator = creatorOpt.get();

        Set<User> members = new HashSet<>();
        for (String email : memberEmails) {
            userRepository.findByEmail(email).ifPresent(members::add);
        }
        members.add(creator); // Add the creator to the group members

        Group group = new Group();
        group.setName(groupName);
        group.setCreator(creator);
        group.setMembers(members);

        return groupRepository.save(group);
    }

    // Send a message to a group
    public GroupMessage sendGroupMessage(String senderEmail, String groupId, String content) {
        Optional<User> senderOpt = userRepository.findByEmail(senderEmail);
        Optional<Group> groupOpt = groupRepository.findById(groupId);

        if (senderOpt.isEmpty()) {
            throw new IllegalArgumentException("Sender not found");
        }
        if (groupOpt.isEmpty()) {
            throw new IllegalArgumentException("Group not found");
        }

        User sender = senderOpt.get();
        Group group = groupOpt.get();

        if (!group.getMembers().contains(sender)) {
            throw new SecurityException("User is not a member of the group");
        }

        GroupMessage message = new GroupMessage();
        message.setGroup(group);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(Instant.now());

        return groupMessageRepository.save(message);
    }

    // Retrieve messages for a group
    public List<GroupMessage> getGroupMessages(String groupId, String userEmail) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        Optional<Group> groupOpt = groupRepository.findById(groupId);

        if (userOpt.isEmpty() || groupOpt.isEmpty()) {
            throw new IllegalArgumentException("User or group not found");
        }

        User user = userOpt.get();
        Group group = groupOpt.get();

        if (!group.getMembers().contains(user)) {
            throw new SecurityException("User is not a member of the group");
        }

        return groupMessageRepository.findByGroup(group);
    }
}
