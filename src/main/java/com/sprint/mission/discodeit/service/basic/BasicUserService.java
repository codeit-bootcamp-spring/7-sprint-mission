package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.entity.dto.userDto.UserCreateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    // 생성
    @Override
    public UserInfoDto createUser(UserCreateDto createDto) {
        userRepository.findByEmail(createDto.getEmail()).ifPresent(user
                -> {throw new DuplicateEmailException("이미 존재하는 이메일");});

        User newUser = User.builder()
                .email(createDto.getEmail())
                .userName(createDto.getUserName())
                .password(createDto.getPassword())
                .phoneNum(createDto.getPhoneNum())
                .build();

        userRepository.save(newUser);
        return UserInfoDto.from(newUser);
    }

    // --- 조회 ---

    // ID로 출력
    @Override
    public Optional<UserInfoDto> findUserInfoById(UUID userId) {
        return userRepository.findById(userId).map(UserInfoDto::from);
    }
    // 이메일로 출력
    @Override
    public Optional<UserInfoDto> findUserInfoByEmail(String email) {
        return userRepository.findByEmail(email).map(UserInfoDto::from);
    }
    // 전체출력
    @Override
    public List<UserInfoDto> findAllUsers() {
        return userRepository.findAll().stream().map(UserInfoDto::from)
                .collect(Collectors.toList());
    }
    // 접근
    public Optional<User> findUserEntityById(UUID userId) {
        return userRepository.findById(userId);
    }

    // --- 수정 ---
    @Override
    public Optional<UserInfoDto> updateProfile(UUID userId, String newUserName, String newPhoneNum) {

        return userRepository.findById(userId).map(user -> {
            user.updateUserName(newUserName);
            user.updatePhoneNum(newPhoneNum);
            userRepository.save(user);
            return UserInfoDto.from(user);
        });
    }

    @Override
    public Optional<UserInfoDto> changePassword(UUID userId, String newPassword) {

        return userRepository.findById(userId).map(user -> {
            user.updatePassword(newPassword);
            userRepository.save(user);
            return UserInfoDto.from(user);
        });
    }

    @Override
    public Optional<UserInfoDto> updateState(UUID userId, UserState newState) {

        return userRepository.findById(userId).map(user -> {
            user.updateState(newState);
            userRepository.save(user);
            return UserInfoDto.from(user);
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
            userRepository.save(userDelete);    // 유저만 저장해서 재시작 시 채널이나 메시지에는 적용이 안됨;;
            return true;
        }).orElse(false);
    }
}