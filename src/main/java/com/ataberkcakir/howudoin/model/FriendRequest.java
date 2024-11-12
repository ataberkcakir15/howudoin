package com.ataberkcakir.howudoin.model;

import com.ataberkcakir.howudoin.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "friend_request")
public class FriendRequest {
    @Id
    private String id;

    @DBRef
    private User sender;

    @DBRef
    private User receiver;

    private RequestStatus status;
}
