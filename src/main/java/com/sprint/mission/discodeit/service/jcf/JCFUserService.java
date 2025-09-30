package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MemoryUserRepository;
import com.sprint.mission.discodeit.service.UserService;


import java.util.*;

public class JCFUserService implements UserService {


    private final MemoryUserRepository userRepository;

    public JCFUserService(MemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }



    @Override
    public void removeUser(User user) {
        userRepository.remove(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateUser(UUID id, UserDto userDto) {
        userRepository.updateUser(id, userDto);
    }

    public void sendMessage(User sender, MessageRoom room, String content){
        Message message1 = new Message(sender, content);
        room.update();
        room.getHistory().add(message1);
    }

    public void sendFriendRequest(User fromUser, User toUser){
        if(getUser(toUser.getId())==null){
            System.out.println("친구 추가하려는 유저가 존재하지 않습니다.");
            return;
        }
        if (fromUser.getFriends().contains(toUser)) {
            System.out.println("이미 친구입니다");
            return;
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(fromUser);
        friendRequest.setReceiver(toUser);

        toUser.getReceivedFriendRequests().add(friendRequest);

    }
    public List<FriendRequest> getReceivedFriendRequests(User user){
        List<FriendRequest> receivedFriendRequests = user.getReceivedFriendRequests();
        return receivedFriendRequests;
    }

    public void acceptFriendRequest(FriendRequest request){
        request.getSender().getFriends().add(request.getReceiver());
        request.getReceiver().getFriends().add(request.getSender());
        System.out.printf(request.getReceiver().getUsername()+"(이)와 "+request.getSender().getUsername()+"(이)가 친구가 되었습니다.\n");
    }



}
