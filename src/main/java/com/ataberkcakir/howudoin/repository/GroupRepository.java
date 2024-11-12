package com.ataberkcakir.howudoin.repository;

import com.ataberkcakir.howudoin.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
