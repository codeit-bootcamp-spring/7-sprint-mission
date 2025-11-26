package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.converter.BinaryContentDtoConverter;
import com.sprint.mission.discodeit.dto.converter.UserDtoConverter;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService{

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    @Transactional
    public UserResponseDto create(CreateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest) {
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
        BinaryContent profileImage = null;

        if (Optional.ofNullable(profileRequest).isPresent()) {
            profileImage = new BinaryContent(
                    profileRequest.fileName(),
                    profileRequest.size(),
                    profileRequest.contentType(),
                    profileRequest.bytes()
            );
        }

        User newUser  = new User(
                userRequest.username(),
                userRequest.email(),
                userRequest.password(),
                profileImage
        );

        UserStatus userStatus = new UserStatus(newUser);
        newUser.setStatus(userStatus);

        userRepository.save(newUser);
        return toDto(newUser);
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return toDto(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(u -> toDto(u))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto update(UUID userId, UpdateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String username = null;
        String email = null;
        String password = null;

        // 프로필 이미지만 전달된 경우 null 참조 방지
        if (Optional.ofNullable(userRequest).isPresent()) {
            username = userRequest.newUsername();
            email = userRequest.newEmail();
            password = userRequest.newPassword();

            // 아이디와 닉네임, 이메일 중복 확인
            if (existsByUsername(username)) {
                throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
            } else if (existsByEmail(email)) {
                throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }

            // 입력된 정보 형태 검증
            Optional.ofNullable(email).ifPresent(e -> UserValidator.validateEmail(e));
            Optional.ofNullable(password).ifPresent(pw -> UserValidator.validatePassword(pw));
        }
        BinaryContent profileImage = null;

        if (Optional.ofNullable(profileRequest).isPresent()) {
            profileImage = new BinaryContent(
                    profileRequest.fileName(),
                    profileRequest.size(),
                    profileRequest.contentType(),
                    profileRequest.bytes()
            );
            binaryContentRepository.save(profileImage);

            // 기존 프로필 이미지 삭제(있는 경우)
            if(user.getProfile() != null) {
                binaryContentRepository.deleteById(user.getProfile().getId());
            }
        }

        user.update(username, email, password, profileImage);
        userRepository.save(user);

        return toDto(user);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto toDto(User user) {

        // 사용자 온라인 상태 저장
        boolean online = user.getStatus().isOnline();

        // 프로필 이미지 저장
        BinaryContent profile = user.getProfile();
        BinaryContentResponseDto binaryContentResponseDto = null;

        if (profile != null) {
            binaryContentResponseDto = BinaryContentDtoConverter.toResponseDto(profile);
        }

        return UserDtoConverter.toResponseDto(user, online, binaryContentResponseDto);
    }

    private boolean existsByUsername(String username) {
        return userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    private boolean existsByEmail(String email) {
        return userRepository.findAll().stream().
                anyMatch(u -> u.getEmail().equals(email));
    }
}
