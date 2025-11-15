package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.exception.DuplicateUserException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.dto.response.UserStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService {


    private final UserRepository userRepository;
    private final BinaryContentService binaryContentService;


    public UserResponse createUser(UserCreateRequest requestDto, MultipartFile file) {
        validateDuplicateEmail(requestDto.email());
        validateDuplicateUsername(requestDto.username());


        User user = new User(
                requestDto.email(),
                requestDto.password(),
                requestDto.username());

        userRepository.save(user);
        binaryContentService.createUserFolder(user.getId());
        if (file != null) {
            BinaryContent content = binaryContentService.saveUserProfile(user.getId(), file);
            user.setProfile(content.getId());
        }
        return UserResponse.from(user);
    }

    public UserResponse updateUserInfo(UUID id, UserUpdateRequest updateDto, MultipartFile file) {

        User user = getById(id);

        if (updateDto.newUsername() != null) {
            user.updateUsername(updateDto.newUsername());
        }
        if (updateDto.newPassword() != null) {
            user.updatePassword(updateDto.newPassword());
        }
        if (updateDto.newEmail() != null) {
            user.updateEmail(updateDto.newEmail());
        }

        if (file != null) {
            BinaryContent content = binaryContentService.saveUserProfile(user.getId(), file);
            user.setProfile(content.getId());
        }

        userRepository.save(user);

        return UserResponse.from(user);
    }


    public void delete(UUID id) {
        User user = getById(id);
        userRepository.remove(user);
        binaryContentService.deleteUserFolder(user.getId());
    }

    public List<UserResponse> getAllUsers() {
        return getAll().stream().map(user -> UserResponse.from(user)).toList();
    }


    public UserResponse login(String loginId, String password) {
        User user = getByUsername(loginId);
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }
        if (!user.checkOnline()) {
            user.markOnline(Instant.now());
            userRepository.save(user);
        }
        return UserResponse.from(user);

    }


    public UserStatusResponse markOnline(UUID uuid, Instant lastAt) {
        User user = getById(uuid);
        user.markOnline(lastAt);
        userRepository.save(user);
        return UserStatusResponse.from(user);
    }

    private User getById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    }

    private User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이디입니다."));
    }


    private List<User> getAll() {
        return userRepository.findAll();
    }


    private void validateDuplicateUsername(String username) {
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new DuplicateUserException("이미 존재하는 사용자 이름입니다.");
        });
    }

    private void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
    }
}
