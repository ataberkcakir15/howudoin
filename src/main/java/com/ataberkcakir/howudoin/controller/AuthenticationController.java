package com.ataberkcakir.howudoin.controller;

import com.ataberkcakir.howudoin.model.AuthenticationRequest;
import com.ataberkcakir.howudoin.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthenticationRequest authRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        return jwtUtil.generateToken(userDetails);
    }
}
