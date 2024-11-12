package com.ataberkcakir.howudoin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GroupDTO {
    private String id;
    private String name;
    private String creatorEmail;
    private Set<String> memberEmails;
}
