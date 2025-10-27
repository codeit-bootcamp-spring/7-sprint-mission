package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService{

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public void createUser(String userName, String nickName, String email, String phoneNum, String userId, String password) {
        User newUser = new User(userName, nickName, email, phoneNum, userId, password);
        userRepository.save(newUser);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByPhone(String phoneNum) {
        return userRepository.findByPhone(phoneNum);
    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 유저가 없습니다."));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User login(String userId, String password) {
        return userRepository.findByUserId(userId)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호를 잘못 입력하셨습니다."));
    }

    @Override
    public String getUserNickName(UUID id) {
        return userRepository.findById(id).getNickName();
    }

    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }

    @Override
    public void updateName(User user, String newName) {
        user.setUserName(newName);
        updateUser(user);
    }

    @Override
    public void updateNickName(User user, String newNickName) {
        user.setNickName(newNickName);
        updateUser(user);
    }

    @Override
    public void updateEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        updateUser(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        updateUser(user);
    }

    @Override
    public void updatePhoneNum(User user, String newPhoneNum) {
        user.setPhoneNum(newPhoneNum);
        updateUser(user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id);
        messageRepository.deleteByUser(user);
        userRepository.deleteById(id);
    }
}
