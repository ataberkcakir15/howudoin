package com.ataberkcakir.howudoin.service;

import com.ataberkcakir.howudoin.dto.UserRegistrationDto;
import com.ataberkcakir.howudoin.model.User;
import com.ataberkcakir.howudoin.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public String registerUser(@Valid UserRegistrationDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return "User with this email already exists";
        }

        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

        User newUser = new User();
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPasswordHash(encryptedPassword);

        userRepository.save(newUser);
        return "User registered successfully";
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            // generate jwt token or handle session management
            return "Login successful";
        }
        else {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
