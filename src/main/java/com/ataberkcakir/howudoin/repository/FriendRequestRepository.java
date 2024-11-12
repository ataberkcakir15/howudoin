package com.ataberkcakir.howudoin.repository;

import com.ataberkcakir.howudoin.enums.RequestStatus;
import com.ataberkcakir.howudoin.model.FriendRequest;
import com.ataberkcakir.howudoin.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findByReceiverAndStatus(User receiver, RequestStatus status);

    List<FriendRequest> findBySenderAndStatus(User sender, RequestStatus status);

    FriendRequest findBySenderAndReceiver(User sender, User receiver);
}
