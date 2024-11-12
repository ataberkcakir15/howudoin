package com.ataberkcakir.howudoin.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    @DBRef
    private User sender;

    @DBRef
    private User receiver;

    private String content;
    private Instant timestamp;
}
