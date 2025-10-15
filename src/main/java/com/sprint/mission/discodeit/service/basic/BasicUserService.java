package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.UserInfo;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.*;

import java.util.*;
import java.util.stream.Collectors;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicUserService(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    // 생성
    @Override
    public UserInfo createUser(String email, String password, String userName, String phoneNum) {
        userRepository.findByEmail(email).ifPresent(user
                -> {throw new DuplicateEmailException("이미 존재하는 이메일");});
        User newUser = new User(email, password, userName, phoneNum);
        userRepository.save(newUser);
        return new UserInfo(newUser);

    }
    @Override
    public UserInfo createUser(String email, String password, String userName) {
        return createUser(email, password, userName, null);
    }

    // 조회
    @Override
    public Optional<UserInfo> findUserInfoById(UUID userId) {
        return userRepository.findById(userId).map(UserInfo::new);
    }

    public Optional<User> findUserEntityById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<UserInfo> findAllUsers() {
        return userRepository.findAll().stream().map(UserInfo::new)
                .collect(Collectors.toList());
    }

    // 수정
    @Override
    public Optional<UserInfo> updateProfile(UUID userId, String newUserName, String newPhoneNum) {

        return userRepository.findById(userId).map(user -> {
            user.updateUserName(newUserName);
            user.updatePhoneNum(newPhoneNum);
            userRepository.save(user);
            return new UserInfo(user);
        }).or(() -> {
            System.out.println("변경 실패");
            return Optional.empty();
        });
    }

    @Override
    public Optional<UserInfo> changePassword(UUID userId, String newPassword) {

        return userRepository.findById(userId).map(user -> {
            user.updatePassword(newPassword);
            userRepository.save(user);
            return new UserInfo(user);
        }).or(()->{
            System.out.println("변경 실패");
            return Optional.empty();
        });
    }

    @Override
    public Optional<UserInfo> updateState(UUID userId, User.State newState) {

        return userRepository.findById(userId).map(user -> {
            user.updateState(newState);
            userRepository.save(user);
            return new UserInfo(user);
        }).or(()->{
            System.out.println("변경 실패");
            return Optional.empty();
        });
    }

    // 논리 삭제
    @Override
    public boolean deleteUser(UUID userId) {

        return userRepository.findById(userId).map(userDelete -> {
            if(channelRepository.existsByAdminId(userId)) {
                throw new IllegalStateException("채널관리자는 삭제할 수 없습니다.");
            }
            userDelete.softDelete();
            userRepository.save(userDelete);
            System.out.println("유저 삭제 완료");
            return true;
        }).orElseGet(() -> {
            System.out.println("삭제 실패");
            return false;
        });
    }
}