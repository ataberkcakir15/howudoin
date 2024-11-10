package com.ataberkcakir.howudoin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    private String username;
    private String password;

    // Default constructor
    public AuthenticationRequest() {
    }

    // Parameterized constructor
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
