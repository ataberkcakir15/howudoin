package com.ataberkcakir.howudoin.controller;

import com.ataberkcakir.howudoin.dto.JwtResponse;
import com.ataberkcakir.howudoin.dto.LoginRequest;
import com.ataberkcakir.howudoin.dto.UserRegistrationDto;
import com.ataberkcakir.howudoin.security.CustomUserDetailsService;
import com.ataberkcakir.howudoin.security.JwtUtil;
import com.ataberkcakir.howudoin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userDto) {
        if (userService.emailExists(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }
        userService.registerUser(userDto);
        return ResponseEntity.ok("User registered successfully");
//        String response = userService.registerUser(userDto);
//        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }
}
