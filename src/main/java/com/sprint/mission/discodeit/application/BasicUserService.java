package com.sprint.mission.discodeit.application;


import com.sprint.mission.discodeit.application.dto.UserDtoMapper;
import com.sprint.mission.discodeit.application.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.exception.DuplicateUserException;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sprint.mission.discodeit.application.dto.UserDtoMapper.userToResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService {


    private final UserRepository userRepository;

    private final FileService fileService;


    public UserResponseDto createUser(UserCreateRequestDto requestDto) throws IOException {
        validateDuplicateEmail(requestDto.email());
        validateDuplicateUsername(requestDto.username());


        User user = new User(
                requestDto.email(),
                requestDto.password(),
                requestDto.username(),
                requestDto.phoneNumber());
        userRepository.save(user);
        fileService.createUserFolder(user.getId());
        if (requestDto.profileImage() != null && !requestDto.profileImage().isEmpty()) {
            BinaryContent content = fileService.saveUserProfile(user.getId(), requestDto.profileImage());
            user.setProfile(content.getId());
        }
        return userToResponseDto(user);
    }

    public UserResponseDto updateUserInfo(UUID id,UserUpdateDto updateDto) throws IOException {

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
        if (updateDto.updateFile() != null) {
            BinaryContent content = fileService.saveUserProfile(user.getId(), updateDto.updateFile());
            user.setProfile(content.getId());
        }
        userRepository.save(user);

        return userToResponseDto(user);
    }


    public void delete(UUID id) {
        User user = getById(id);
        userRepository.remove(user);
        fileService.deleteUserFolder(user.getId());
    }

    public UserResponseDto getUser(UserRequestDto requestDto) {
        User user;
        if (requestDto.id() == null) {
            user = getByUsername(requestDto.username());
        } else {
            user = getById(requestDto.id());
        }
        return userToResponseDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return getAll().stream().map(user -> UserDtoMapper.userToResponseDto(user)).toList();
    }


    public UserResponseDto login(String loginId, String password) {
        User user = getByUsername(loginId);
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }
        if (!user.checkOnline()) {
            user.markOnline(Instant.now());
            userRepository.save(user);
        }
        return userToResponseDto(user);

    }

    public void logout(UUID userId) {
        User user = getById(userId);
        user.markOffline();
        userRepository.save(user);
    }

    public void markUserStatus(UUID uuid, Instant lastAt) {
        User user = getById(uuid);
        user.markOnline(lastAt);
        userRepository.save(user);


        
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
