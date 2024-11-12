package com.ataberkcakir.howudoin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GroupCreationRequest {
    @NotBlank(message = "Group name is required")
    private String name;

    @NotEmpty(message = "At least one member is required")
    private Set<String> memberEmails;
}
