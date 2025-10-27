package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserDto createUserDto) {
        if(userRepository.findByUsername(createUserDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 유저입니다." + createUserDto.getUsername());
        }
        if(userRepository.findByEmail(createUserDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 등록된 이메일입니다." + createUserDto.getUsername());
        }
        User user = new User(
                createUserDto.getUsername(), createUserDto.getEmail(), createUserDto.getPassword(),
                createUserDto.getPhoneNumber(), createUserDto.getPronoun());


        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(UUID userId) { // 단건 검색
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 유저: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UpdateUserDto updateUserDto) {
        User user = userRepository.findById(updateUserDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 유저: " + updateUserDto.getUserId()));
        user.updateUser(updateUserDto.getUsername(),
                updateUserDto.getPassword(),
                updateUserDto.getEmail(),
                updateUserDto.getPhoneNumber(),
                updateUserDto.getPronoun()
        );
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("찾을 수 없는 유저: " + userId);
        }

        userRepository.deleteById(userId);
    }

    @Override
    public boolean isExistsUser(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public void addChannelToUser(User user, Channel channel) {
        if (!user.getJoinChannels().contains(channel)) {
            user.getJoinChannels().add(channel);
            user.touch();
            userRepository.save(user);
        }

    }

    @Override
    public void removeChannelFromAllUsers(Channel channel) {
        for (User user : this.getAllUsers()) {
            user.leaveChannel(channel);
            user.touch();
            userRepository.save(user);
        }
    }


}
