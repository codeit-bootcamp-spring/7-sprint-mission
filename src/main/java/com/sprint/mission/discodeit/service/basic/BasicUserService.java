package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 TODO: 프로필 삭제
 */
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto, UUID profileId) {
        // 요구사항 - 유저 이름과 이메일은 다른 유저와 같으면 안된다.
        if (!userRepository.findByName(userCreateRequestDto.username()).isEmpty()) {
            throw new IllegalArgumentException("존재하는 유저입니다!");
        }

        if (userRepository.findByEmail(userCreateRequestDto.email()).isPresent()) {
            throw new IllegalArgumentException("존재하는 이메일입니다!");
        }

        User user = new User(
                userCreateRequestDto.username(),
                userCreateRequestDto.password(),
                userCreateRequestDto.email(),
                profileId
        );
        user = userRepository.save(user);

        // 요구사항 - userStatus 같이 생성
        userStatusRepository.save(new UserStatus(user.getId()));
        boolean online = isOnline(user.getId());

        return UserResponseDto.from(user, online);
    }

    @Override
    public UserResponseDto get(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        boolean online = isOnline(user.getId());
        return UserResponseDto.from(user, online);
    }

    @Override
    public List<UserResponseDto> getAll() {
        userRepository.findAll();
    }

    @Override
    public User update(UserUpdateRequestDto userUpdateRequestDto, UUID profileId) {
        User user = userRepository.findById(userUpdateRequestDto.id())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // 유저 이름이 비어있지 않고, 기존 이름과 다르다면 수정!
        if(userUpdateRequestDto.username() != null && !userUpdateRequestDto.username().equals(user.getUsername())){
            if(!userRepository.findByName(userUpdateRequestDto.username()).isEmpty()) {
                throw new IllegalArgumentException("존재하는 유저이름입니다!");
            }
            user.setUsername(userUpdateRequestDto.username());
        }

        // 유저 이메일이 비어있지 않고, 기존 이메일과 다르다면 수정!
        if(userUpdateRequestDto.email() != null && !userUpdateRequestDto.email().equals(user.getEmail())){
            if(userRepository.findByEmail(userUpdateRequestDto.email()).isPresent()) {
                throw new IllegalArgumentException("존재하는 이메일입니다!");
            }
            user.setEmail(userUpdateRequestDto.email());
        }

        if(userUpdateRequestDto.password() != null) {
            user.setPassword(userUpdateRequestDto.password());
        }

            if(profileId != null && !profileId.equals(user.getProfileId())){
                user.setProfileId(profileId);
            }
        return userRepository.save(user);
    }

    @Override
    public boolean delete(UUID userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userStatusRepository.findByUserId(userid)
                .ifPresent(u -> userStatusRepository.deleteById(u.getId()));
        return userRepository.deleteById(userid);
    }

    // 이름으로 조회
    @Override
    public List<User> getUsersByName(String username) {
        Objects.requireNonNull(username);
        return userRepository.findByName(username);
    }

    // 이메일로 조회
    @Override
    public Optional<User> getUsersByEmail(String email) {
        Objects.requireNonNull(email);
        return userRepository.findByEmail(email);
    }

    // 로그인
    @Override
    public void login(UUID userId) {
        userStatusRepository.findByUserId(userId).ifPresentOrElse(u -> {
            u.timeUpdated();
            userStatusRepository.save(u);
        }, () -> userStatusRepository.save(new UserStatus(userId)));
    }
    // 로그아웃
    @Override
    public void logout(UUID userId) {
    }

    @Override
    public boolean isOnline(UUID userId) {
        Objects.requireNonNull(userId);
        return userStatusRepository.findByUserId(userId)
                .map(status -> status.isOnlineNow())
                .orElse(false);
    }
}