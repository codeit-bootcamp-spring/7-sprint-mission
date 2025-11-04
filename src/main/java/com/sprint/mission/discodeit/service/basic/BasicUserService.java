package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 controller가 없으니 Response를 서비스에서 대신 반환한다!
 반환 종류는 임의로 선정함!
 TODO: 프로필 삭제
 */
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserCreateRequestDto userCreateRequestDto) {
        // 요구사항 - 유저 이름과 이메일은 다른 유저와 같으면 안된다.
        if (!userRepository.findByName(userCreateRequestDto.username()).isEmpty()) {
            throw new IllegalArgumentException("존재하는 유저입니다!");
        }

        if (userRepository.findByEmail(userCreateRequestDto.email()).isPresent()) {
            throw new IllegalArgumentException("존재하는 이메일입니다!");
        }

        // 요구사항 - 프로밀 이미지를 등록할 수 있다!
        UUID profileId = null;
        if(userCreateRequestDto.profileData() != null){
            BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(
                    userCreateRequestDto.profileFileName(),
                    userCreateRequestDto.profileContentType(),
                    userCreateRequestDto.profileData()));
            profileId = binaryContent.getId();
        }

        User user = User.builder()
                .username(userCreateRequestDto.username())
                .password(userCreateRequestDto.password())
                .email(userCreateRequestDto.email())
                .profileId(profileId)
                .build();
        user = userRepository.save(user);

        // 요구사항 - userStatus 같이 생성
        userStatusRepository.save(new UserStatus(user.getId()));

        return user;
    }

    @Override
    public User get(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UserUpdateRequestDto userUpdateRequestDto) {
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

        if(userUpdateRequestDto.profileData() != null){
            if(user.getProfileId() != null){
                binaryContentRepository.deleteById(user.getProfileId());
            }
            BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(
                    userUpdateRequestDto.profileFileName(),
                    userUpdateRequestDto.profileContentType(),
                    userUpdateRequestDto.profileData()
            ));
            user.setProfileId(binaryContent.getId());
        }
        return userRepository.save(user);
    }

    @Override
    public boolean delete(UUID userid) {
        User user = userRepository.findById(userid).orElseThrow(() -> new NoSuchElementException("User not found"));
        if(user.getProfileId() != null){
            binaryContentRepository.deleteById(user.getProfileId());
        }
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

    // 특정 상태만 조회
    @Override
    public List<User> getUsersByState(UserState userState) {
        Objects.requireNonNull(userState);
        return userRepository.findByState(userState);
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