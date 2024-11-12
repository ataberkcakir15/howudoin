package com.ataberkcakir.howudoin.repository;

import com.ataberkcakir.howudoin.model.Group;
import com.ataberkcakir.howudoin.model.GroupMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupMessageRepository extends MongoRepository<GroupMessage, String> {
    List<GroupMessage> findByGroup(Group group);
}
