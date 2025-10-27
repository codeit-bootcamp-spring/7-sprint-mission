package com.sprint.mission.discodeit.application;


import com.sprint.mission.discodeit.application.common.FileManager;
import com.sprint.mission.discodeit.application.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.UserRepository;
import com.sprint.mission.discodeit.domain.user.exception.DuplicateUserException;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {


    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final FileManager fileManager;


    @Override
    public UserResponseDto createUser(UserCreateRequestDto requestDto) throws IOException {
        validateDuplicateEmail(requestDto.email());
        validateDuplicateUsername(requestDto.username());
        User user = new User(
                requestDto.email(),
                requestDto.password(),
                requestDto.username(),
                requestDto.phoneNumber());
        userRepository.save(user);
        createUserStatue(user.getId());

        //유저 전용 폴더 생성 & 파일 저장
        fileManager.createUserFolder(user.getId());
        if (requestDto.profileImage() != null) {
            fileManager.saveUserFile(user.getId(), requestDto.profileImage());
        }

        return userToResponseDto(user);
    }

    @Override
    public UserResponseDto getUser(UserRequestDto requestDto) {
        User user = findByUsername(requestDto.username());
        return userToResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::userToResponseDto).toList();
    }

    @Override
    public UserResponseDto updateUserInfo(UserUpdateDto updateDto) throws IOException {
        User user = findById(updateDto.id());
        if (updateDto.updateFile() != null) {
            fileManager.saveUserFile(user.getId(), updateDto.updateFile());
        }
        if (updateDto.username() != null) {
            user.updateUsername(updateDto.username());
        }
        if (updateDto.password() != null) {
            user.updatePassword(updateDto.password());
        }
        if (updateDto.phoneNumber() != null) {
            user.updatePhoneNumber(updateDto.phoneNumber());
        }

        userRepository.save(user);
        return userToResponseDto(user);
    }

    @Override
    public void delete(UserRequestDto requestDto) {
        userRepository.remove(findById(requestDto.id()));
        fileManager.deleteUserFolder(requestDto.id());
        UserStatus userStatus = userStatusRepository.findByUserId(requestDto.id())
                .orElseThrow(() -> new NoSuchElementException());
        userStatusRepository.remove(userStatus);

    }

    private User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    //이메일로 유저 정보 가져오기
    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    private User findByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }


    private void validateNotDuplicateUser(String email) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
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

    private UserResponseDto userToResponseDto(User user) {
        return new UserResponseDto(user.getEmail(), user.getUsername(), user.getPhoneNumber());
    }

    //UserStatus 관련 CRUD
    private UserStatus createUserStatue(UUID userId) {
        UserStatus userStatus = UserStatus.create(userId);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    private UserStatus findUserStatusByUserId(UUID id) {
        return userStatusRepository.findByUserId(id).orElse(null);
    }

    //예외는 컨트롤러로 다 던져야 하나? 나중에 @ControllerAdvice로 처리할 수 있게? 우선은 그렇게 해보자
    //멘토님에게 물어보기
}
