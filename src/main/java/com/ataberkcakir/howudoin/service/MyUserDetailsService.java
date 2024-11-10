package com.ataberkcakir.howudoin.service;

import com.ataberkcakir.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // You would typically load the user from a database here
        // For example:
        // return userRepository.findByUsername(username)
        //        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // For demonstration, creating a hardcoded user (to be replaced by actual user retrieval)
        return org.springframework.security.core.userdetails.User
                .withUsername("user")
                .password(new BCryptPasswordEncoder().encode("password")) // `{noop}` indicates no encoding; replace with encoded password in production
                .roles("USER")
                .build();
    }
}

