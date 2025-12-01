package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/*
 TODO: 프로필 삭제
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;
    private final BinaryContentService binaryContentService;

    @Transactional
    @Override
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto,
                                  BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        // 요구사항 - 유저 이름과 이메일은 다른 유저와 같으면 안된다.
        if (userRepository.existsByUsername(userCreateRequestDto.username())) {
            throw new IllegalArgumentException("존재하는 유저입니다!");
        }

        if (userRepository.existsByEmail(userCreateRequestDto.email())) {
            throw new IllegalArgumentException("존재하는 이메일입니다!");
        }

        BinaryContent profile = null;
        if (binaryContentCreateRequestDto != null) {
            BinaryContentResponseDto binaryContentResponseDto =
                    binaryContentService.create(binaryContentCreateRequestDto);
            profile = binaryContentRepository.getReferenceById(binaryContentResponseDto.id());
        }

        User user = new User(
                userCreateRequestDto.username(),
                userCreateRequestDto.password(),
                userCreateRequestDto.email(),
                profile
        );


        User save = userRepository.save(user);

        // 요구사항 - userStatus 같이 생성
        userStatusRepository.save(new UserStatus(user));

        boolean online = true;

        return userMapper.toDto(save, online);
    }

    @Override
    public UserResponseDto get(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        boolean online = isOnline(user.getId());
        return userMapper.toDto(user, online);
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.toDto(user, isOnline(user.getId())))
                .toList();
    }

    @Transactional
    @Override
    public UserResponseDto update(UUID userId, UserUpdateRequestDto userUpdateRequestDto,
                                  BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        User user = userRepository.findById(Objects.requireNonNull(userId))
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // 유저 이름이 비어있지 않고, 기존 이름과 다르다면 수정!
        if(userUpdateRequestDto.newUsername() != null &&
                !userUpdateRequestDto.newUsername().equals(user.getUsername())){
            if(userRepository.findByUsername(userUpdateRequestDto.newUsername())
                    .stream()
                    .anyMatch(u -> !u.getId().equals(userId))) {
                throw new IllegalArgumentException("존재하는 유저이름입니다!");
            }
            user.setUsername(userUpdateRequestDto.newUsername());
        }

        // 유저 이메일이 비어있지 않고, 기존 이메일과 다르다면 수정!
        if(userUpdateRequestDto.newEmail() != null &&
                !userUpdateRequestDto.newEmail().equals(user.getEmail())){
            if(userRepository.findByEmail(userUpdateRequestDto.newEmail())
                    .filter(u -> !u.getId().equals(userId))
                    .isPresent()) {
                throw new IllegalArgumentException("존재하는 이메일입니다!");
            }
            user.setEmail(userUpdateRequestDto.newEmail());
        }

        if(userUpdateRequestDto.newPassword() != null) {
            user.setPassword(userUpdateRequestDto.newPassword());
        }

        if(binaryContentCreateRequestDto != null){
            UUID oldProfileId = (user.getProfile()) != null ? user.getProfile().getId() : null;
            BinaryContentResponseDto binaryContentResponseDto = binaryContentService.create(binaryContentCreateRequestDto);
            BinaryContent newProfile = binaryContentRepository.getReferenceById(binaryContentResponseDto.id());
            user.setProfile(newProfile);

            if(oldProfileId != null) {
                binaryContentService.delete(oldProfileId);
            }
        }

        User save = userRepository.save(user);
        boolean online = isOnline(save.getId());

        return userMapper.toDto(save, online);
    }

    @Transactional
    @Override
    public boolean delete(UUID userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userRepository.delete(user);

        return true;
    }

    // 이름으로 조회
    @Override
    public List<UserResponseDto> getUsersByName(String username) {
        return userRepository.findByUsername(Objects.requireNonNull(username))
                .stream()
                .map(user -> userMapper.toDto(user, isOnline(user.getId())))
                .toList();
    }

    // 이메일로 조회
    @Override
    public Optional<UserResponseDto> getUsersByEmail(String email) {
        return userRepository.findByEmail(Objects.requireNonNull(email))
                .map(user -> userMapper.toDto(user, isOnline(user.getId())));
    }

    // 로그인
    @Transactional
    @Override
    public void login(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userStatusRepository.findByUserId(Objects.requireNonNull(userId))
                .ifPresentOrElse(u -> {
                    u.timeUpdated();
                    }, () -> userStatusRepository.save(new UserStatus(user)));
    }
    // 로그아웃
    @Override
    public void logout(UUID userId) {
    }

    @Override
    public boolean isOnline(UUID userId) {
        return userStatusRepository.findByUserId(Objects.requireNonNull(userId))
                .map(status -> status.isOnlineNow())
                .orElse(false);
    }
}