package com.ataberkcakir.howudoin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    private String email;
    private String password;

    // Default constructor
    public AuthenticationRequest() {
    }

    // Parameterized constructor
    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
