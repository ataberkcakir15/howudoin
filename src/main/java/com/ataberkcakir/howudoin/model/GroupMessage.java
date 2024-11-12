package com.ataberkcakir.howudoin.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "group_messages")
public class GroupMessage {
    @Id
    private String id;

    @DBRef
    private Group group;

    @DBRef
    private User sender;

    private String content;
    private Instant timestamp;

}
