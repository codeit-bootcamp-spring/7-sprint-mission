package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(String username, String nickName) {
        User user = new User(username, nickName);
        return userRepository.save(user);
    }

    @Override
    public User findUser(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(String username, UUID userId, String nickName) {
        User byId = userRepository.findById(userId);
        byId.setUsername(username);
        byId.setNickName(nickName);
        return userRepository.save(byId);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.delete(userId);
    }
}
