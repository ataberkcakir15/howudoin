package com.ataberkcakir.howudoin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MessageDTO {
    private String id;
    private String senderEmail;
    private String receiverEmail;
    private String content;
    private Instant timestamp;
}
