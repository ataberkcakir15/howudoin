package com.ataberkcakir.howudoin.dto;

import com.ataberkcakir.howudoin.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestDTO {
    private String id;
    private UserDTO sender;
    private UserDTO receiver;
    private RequestStatus status;
}
