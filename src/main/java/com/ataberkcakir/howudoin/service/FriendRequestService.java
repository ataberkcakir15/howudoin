package com.ataberkcakir.howudoin.service;

import com.ataberkcakir.howudoin.enums.RequestStatus;
import com.ataberkcakir.howudoin.model.FriendRequest;
import com.ataberkcakir.howudoin.model.User;
import com.ataberkcakir.howudoin.repository.FriendRequestRepository;
import com.ataberkcakir.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    // Send a friend request
    public String sendFriendRequest(String senderEmail, String receiverEmail) {
        Optional<User> senderOpt = userRepository.findByEmail(senderEmail);
        Optional<User> receiverOpt = userRepository.findByEmail(receiverEmail);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return "User not found";
        }

        User sender = senderOpt.get();
        User receiver = receiverOpt.get();

        if (sender.equals(receiver)) {
            return "You cannot send a friend request to yourself.";
        }

        // Check if a request already exists
        FriendRequest existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        if (existingRequest != null) {
            return "Friend request already sent.";
        }

        // Create a new friend request
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(RequestStatus.PENDING);

        friendRequestRepository.save(friendRequest);

        return "Friend request sent.";
    }

    // Accept a friend request
    public String acceptFriendRequest(String receiverUsername, String senderUsername) {
        Optional<User> receiverOpt = userRepository.findByEmail(receiverUsername);
        Optional<User> senderOpt = userRepository.findByEmail(senderUsername);

        if (receiverOpt.isEmpty() || senderOpt.isEmpty()) {
            return "User not found.";
        }

        User receiver = receiverOpt.get();
        User sender = senderOpt.get();

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);

        if (friendRequest == null || friendRequest.getStatus() != RequestStatus.PENDING) {
            return "Friend request not found.";
        }

        friendRequest.setStatus(RequestStatus.ACCEPTED);
        friendRequestRepository.save(friendRequest);

        // Update friends lists
        if (!receiver.getFriends().contains(sender)) {
            receiver.getFriends().add(sender);
            userRepository.save(receiver);
        }
        if (!sender.getFriends().contains(receiver)) {
            sender.getFriends().add(receiver);
            userRepository.save(sender);
        }

        return "Friend request accepted.";
    }

    // Reject a friend request
    public String rejectFriendRequest(String receiverEmail, String senderEmail) {
        Optional<User> receiverOpt = userRepository.findByEmail(receiverEmail);
        Optional<User> senderOpt = userRepository.findByEmail(senderEmail);

        if (receiverOpt.isEmpty() || senderOpt.isEmpty()) {
            return "User not found.";
        }

        User receiver = receiverOpt.get();
        User sender = senderOpt.get();

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);

        if (friendRequest == null || friendRequest.getStatus() != RequestStatus.PENDING) {
            return "Friend request not found.";
        }

        friendRequest.setStatus(RequestStatus.REJECTED);
        friendRequestRepository.save(friendRequest);

        return "Friend request rejected.";
    }

    // Get pending friend requests for a user
    public List<FriendRequest> getPendingRequests(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return List.of(); // Return an empty list or handle as needed
        }
        User user = userOpt.get();
        return friendRequestRepository.findByReceiverAndStatus(user, RequestStatus.PENDING);
    }

    // Get friends list
    public Set<User> getFriendsList(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return Set.of(); // Return an empty list or handle as needed
        }
        User user = userOpt.get();
        return user.getFriends();
    }
}
