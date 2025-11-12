package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.service.dto.request.ProfileRequest;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.service.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.exception.DuplicateUserException;
import com.sprint.mission.discodeit.repository.UserRepository;
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


    public UserResponse createUser(UserCreateRequest requestDto, MultipartFile profileRequest) {
        validateDuplicateEmail(requestDto.email());
        validateDuplicateUsername(requestDto.username());
        System.out.println("BasicUserService.createUser");
        //우선 폰넘버가 안 넘어와서 임의로 넣었음
        User user = new User(
                requestDto.email(),
                requestDto.password(),
                requestDto.username(),
                "010-1234-1234");
        userRepository.save(user);
        binaryContentService.createUserFolder(user.getId());
        if (profileRequest != null && !profileRequest.isEmpty()) {
            BinaryContent content = binaryContentService.saveUserProfile(user.getId(), profileRequest);
            user.setProfile(content.getId());
        }
        return UserResponse.from(user);
    }

    public UserResponse updateUserInfo(UUID id, UserUpdateReq updateDto, ProfileRequest profileRequest) {

        User user = getById(id);

        if (updateDto.username() != null) {
            user.updateUsername(updateDto.username());
        }
        if (updateDto.password() != null) {
            user.updatePassword(updateDto.password());
        }
        if (updateDto.phoneNumber() != null) {
            user.updatePhoneNumber(updateDto.phoneNumber());
        }
        if (profileRequest.profileImage() != null) {
            BinaryContent content = binaryContentService.saveUserProfile(user.getId(), profileRequest.profileImage());
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

    public UserResponse getUser(UserRequest requestDto) {
        User user;
        if (requestDto.id() == null) {
            user = getByUsername(requestDto.username());
        } else {
            user = getById(requestDto.id());
        }
        return UserResponse.from(user);
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

    public void logout(UUID userId) {
        User user = getById(userId);
        user.markOffline();
        userRepository.save(user);
    }

    public UserStatusResponse markUserStatus(UUID uuid, Instant lastAt) {
        User user = getById(uuid);
        user.markOnline(lastAt);
        userRepository.save(user);
        return UserStatusResponse.from(user);
    }

    private User getById(UUID userId) {
        log.info("findUser");
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
