package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserDto createUserDto) {
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
        if (!userRepository.existsById(updateUserDto.getUserId())) {
            throw new NoSuchElementException("찾을 수 없는 유저: " + updateUserDto.getUserId());
        }

        User user = this.getUser(updateUserDto.getUserId());
        user.updateUser(updateUserDto);
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
    public void addChannelToUser(UUID userId, UUID channelId) {
        User user = getUser(userId);
        if (!user.getJoinChannels().contains(channelId)) {
            user.addChannel(channelId);
            user.touch();
            userRepository.save(user);
        }
    }

    @Override
    public void removeChannelFromAllUsers(UUID channelId) {
        for (User user : this.getAllUsers()) {
            user.removeChannel(channelId);
            user.touch();
            userRepository.save(user);
        }
    }
}
