package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * FileUserService
 * -----------------
 * UserService 인터페이스의 파일 기반 구현체로,
 * FileUserRepository를 주입받아 메시지 처리 로직을 수행합니다.
 *
 * - 기능은 JCFUserService와 동일하나,
 *   데이터 저장 및 조회가 파일(.sav) 단위로 이루어집니다.
 */
public class FileUserService implements UserService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public FileUserService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void createUser(String userName, String nickName, String email, String phoneNum, String userId, String password) {
        User newUser = new User(userName, nickName, email, phoneNum, userId, password);
        userRepository.save(newUser);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserByPhone(String phoneNum) {
        return userRepository.findByPhone(phoneNum).orElse(null);
    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User login(String userId, String password) {
        return userRepository.findByUserId(userId)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public String getUserNickName(UUID id) {
        return userRepository.findById(id)
                .map(user -> user.getNickName())
                .orElse(null);
    }

    @Override
    public void updateUser(User user) {
        // ifPresent: 해당 id를 가진 유저가 있다면 true, 아니라면 false
        userRepository.findById(user.getId()).ifPresent(existing -> {
            if (user.getUserName() == null) user.setUserName(existing.getUserName());
            if (user.getNickName() == null) user.setNickName(existing.getNickName());
            if (user.getEmail() == null) user.setEmail(existing.getEmail());
            if (user.getPhoneNum() == null) user.setPhoneNum(existing.getPhoneNum());
            if (user.getPassword() == null) user.setPassword(existing.getPassword());

            userRepository.update(user);
        });
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.findById(id).ifPresent(user -> {
            messageRepository.deleteByUser(user);
            userRepository.deleteById(id);
        });
    }
}
