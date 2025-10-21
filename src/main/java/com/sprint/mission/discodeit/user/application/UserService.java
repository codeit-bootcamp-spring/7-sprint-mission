package com.sprint.mission.discodeit.user.application;


import com.sprint.mission.discodeit.friendrequest.domain.FriendRequest;

import com.sprint.mission.discodeit.user.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserService{

    void save(User userDto);

    void remove(User userDto);

    User findById(UUID id);

    List<User> findAll();

    User findByEmail(String email);

    FriendRequest sendFriendRequest(User sender,User target);
    List<FriendRequest> getSentFriendRequests(User user);
    List<FriendRequest> getReceivedFriendRequests(User user);
    void acceptFriendRequest(User user, FriendRequest request);
    void register(User user);
}
