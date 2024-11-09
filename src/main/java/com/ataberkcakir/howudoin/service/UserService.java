package com.ataberkcakir.howudoin.service;

import com.ataberkcakir.howudoin.model.User;
import com.ataberkcakir.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String firstName, String lastName, String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()) {
            return "User with this email already exists";
        }

        String encryptedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setCreatedAt(LocalDateTime.now().toString());

        userRepository.save(newUser);
        return "User registered successfully";
    }
}
