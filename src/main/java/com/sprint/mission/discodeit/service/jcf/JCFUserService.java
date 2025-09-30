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








}
