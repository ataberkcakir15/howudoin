package com.ataberkcakir.howudoin.controller;

import com.ataberkcakir.howudoin.dto.FriendRequestDTO;
import com.ataberkcakir.howudoin.dto.UserDTO;
import com.ataberkcakir.howudoin.model.FriendRequest;
import com.ataberkcakir.howudoin.model.User;
import com.ataberkcakir.howudoin.service.FriendRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @Autowired
    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    // send a friend request
    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam String toEmail, Authentication authentication) {
        String fromEmail = authentication.getName();
        String result = friendRequestService.sendFriendRequest(fromEmail, toEmail);
        if (result.equals("User not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.equals("You cannot send a friend request to yourself.") ||
                result.equals("Friend request already sent.")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } else {
            return ResponseEntity.ok(result);
        }
    }

    // Accept a friend request
    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam String fromEmail, Authentication authentication) {
        String toEmail = authentication.getName();
        String result = friendRequestService.acceptFriendRequest(toEmail, fromEmail);

        if (result.equals("User not found.") || result.equals("Friend request not found.")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            return ResponseEntity.ok(result);
        }
    }

    // Reject a friend request
    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestParam String fromEmail, Authentication authentication) {
        String toEmail = authentication.getName();
        String result = friendRequestService.rejectFriendRequest(toEmail, fromEmail);
        return ResponseEntity.ok(result);
    }

    // Get pending friend requests
    @GetMapping("/requests")
    public ResponseEntity<?> getPendingRequests(Authentication authentication) {
        String email = authentication.getName();
        List<FriendRequest> requests = friendRequestService.getPendingRequests(email);

        // Convert to DTOs
        List<FriendRequestDTO> requestDTOs = requests.stream()
                .map(request -> {
                    FriendRequestDTO dto = new FriendRequestDTO();
                    dto.setId(request.getId());
                    dto.setSender(convertToDTO(request.getSender()));
                    dto.setReceiver(convertToDTO(request.getReceiver()));
                    dto.setStatus(request.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestDTOs);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    // Get friends list
    @GetMapping("/list")
    public ResponseEntity<?> getFriendsList(Authentication authentication) {
        String email = authentication.getName();
        Set<User> friends = friendRequestService.getFriendsList(email);

        // Convert Set<User> to List<UserDTO> to avoid serialization issues
        List<UserDTO> friendsDTO = friends.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(friendsDTO);
    }
}
