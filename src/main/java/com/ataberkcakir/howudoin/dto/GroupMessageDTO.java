package com.ataberkcakir.howudoin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class GroupMessageDTO {
    private String id;
    private String groupId;
    private String senderEmail;
    private String content;
    private Instant timestamp;
}
