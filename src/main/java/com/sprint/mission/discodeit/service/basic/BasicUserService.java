package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.validator.UserValidator;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService{

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(CreateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest) {
        // 1. username/nickname/email 중복 검사
        if (existsByUsername(userRequest.username())) {
            throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
        } else if (existsByEmail(userRequest.email())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 2. 입력된 정보 형태 검증
        Optional.ofNullable(userRequest.email()).ifPresent(e -> UserValidator.validateEmail(e));
        Optional.ofNullable(userRequest.password()).ifPresent(pw -> UserValidator.validatePassword(pw));

        // 3. 선택적 프로필 이미지 처리
        UUID profileImageId = null;

        if (Optional.ofNullable(profileRequest).isPresent()) {
            BinaryContent profileImage = new BinaryContent(
                    profileRequest.fileName(),
                    profileRequest.contentType(),
                    profileRequest.bytes()
            );
            binaryContentRepository.save(profileImage);
            profileImageId = profileImage.getId();
        }

        User newUser  = new User(
                userRequest.username(),
                userRequest.username(),
                userRequest.email(),
                "010-0000-0000",
                userRequest.username(),
                userRequest.password(),
                profileImageId
        );

        userStatusRepository.save(new UserStatus(newUser.getId()));
        userRepository.save(newUser);

        return newUser;
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean active = userStatusRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND))
                .isOnline();

        return UserResponseDto.from(user, active);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean active = userStatusRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND))
                .isOnline();

        return UserResponseDto.from(user, active);
    }

    @Override
    public UserResponseDto findByPhoneNum(String phoneNum) {
        User user = userRepository.findByPhone(phoneNum)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean active = userStatusRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND))
                .isOnline();

        return UserResponseDto.from(user, active);
    }

    @Override
    public UserResponseDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean active = userStatusRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND))
                .isOnline();

        return UserResponseDto.from(user, active);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(u -> UserResponseDto.from(
                        u,
                        userStatusRepository.findByUserId(u.getId())
                                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND))
                                .isOnline()))
                .collect(Collectors.toList());
    }

    @Override
    public String findNickNameById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                .getNickName();
    }

    @Override
    public User update(UUID userId, UpdateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String realName = null;
        String nickName = null;
        String email = null;
        String phoneNum = null;
        String username = null;
        String password = null;

        // 프로필 이미지만 전달된 경우 null 참조 방지
        if (Optional.ofNullable(userRequest).isPresent()) {
            realName = userRequest.newUsername();
            nickName = userRequest.newUsername();
            email = userRequest.newEmail();
            phoneNum = "010-0000-0000";
            username = userRequest.newUsername();
            password = userRequest.newPassword();

            // 아이디와 닉네임, 이메일 중복 확인
            if (existsByUsername(username)) {
                throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
            } else if (existsByEmail(email)) {
                throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }

            // 입력된 정보 형태 검증
            Optional.ofNullable(nickName).ifPresent(n -> UserValidator.validateNickname(n));
            Optional.ofNullable(email).ifPresent(e -> UserValidator.validateEmail(e));
            Optional.ofNullable(phoneNum).ifPresent(pn -> UserValidator.validatePhoneNum(pn));
            Optional.ofNullable(password).ifPresent(pw -> UserValidator.validatePassword(pw));
        }

        UUID profileImageId = null;

        if (Optional.ofNullable(profileRequest).isPresent()) {
            BinaryContent profileImage = new BinaryContent(
                    profileRequest.fileName(),
                    profileRequest.contentType(),
                    profileRequest.bytes()
            );
            binaryContentRepository.save(profileImage);
            profileImageId = profileImage.getId();
            binaryContentRepository.delete(user.getProfileId()); // 기존 프로필 이미지 삭제
        }

        user.update(realName, nickName, email, phoneNum, username, password, profileImageId);
        userRepository.update(user);
        return user;
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        messageRepository.deleteByUser(id);
        userStatusRepository.deleteById(user.getId());
        binaryContentRepository.delete(user.getProfileId());
        userRepository.deleteById(id);
    }

    @Override
    public boolean isPasswordMatch(UUID userId, String password) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                .getPassword().equals(password);
    }

    private boolean existsByUsername(String username) {
        return userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    private boolean existsByNickName(String NickName) {
        return userRepository.findAll().stream().
                anyMatch(u -> u.getNickName().equals(NickName));
    }

    private boolean existsByEmail(String email) {
        return userRepository.findAll().stream().
                anyMatch(u -> u.getEmail().equals(email));
    }
}
