package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.global.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.global.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService{

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserMapper userMapper;

    private final BinaryContentStorage binaryContentStorage;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto create(CreateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest) {

        log.debug("신규 사용자 생성 요청: username = {}, email = {}",
                userRequest.username(), userRequest.email());

        // username, email 중복 검사
        validateUsernameDuplicate(userRequest.username());
        validateEmailDuplicate(userRequest.email());

        // 선택적 프로필 이미지 처리
        BinaryContent profileImage = saveProfileImage(null, profileRequest);

        // 사용자 생성
        User newUser  = new User(
                userRequest.username(),
                userRequest.email(),
                passwordEncoder.encode(userRequest.password()),
                profileImage,
                Role.USER
        );

        // 사용자 저장
        User saved = userRepository.save(newUser);

        log.info("새 유저 생성 완료: userId = {}, username = {}, email = {}",
                saved.getId(), saved.getUsername(), saved.getEmail());

        return userMapper.toResponseDto(newUser);
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", userId)
                ));

        return userMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        // fetch join 적용
        return userRepository.findAllWithProfile().stream()
                .map(u -> userMapper.toResponseDto(u))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.getUserDto.id")
    public UserResponseDto update(UUID userId, UpdateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", userId)
                ));

        log.debug("사용자 정보 수정 요청: userId = {}, username = {}", user.getId(), user.getUsername());

        String username = null;
        String email = null;
        String password = null;

        // 프로필 이미지만 전달된 경우 null 참조 방지
        if (Optional.ofNullable(userRequest).isPresent()) {
            username = userRequest.newUsername();
            email = userRequest.newEmail();
            password = userRequest.newPassword();

            // username, email 중복 검사 (사용자의 기존 정보인 경우 제외)
            if(!user.getUsername().equals(username)) validateUsernameDuplicate(username);
            if(!user.getEmail().equals(email)) validateEmailDuplicate(email);
        }

        // 선택적 프로필 이미지 처리
        BinaryContent profileImage = saveProfileImage(user.getProfile(), profileRequest);

        user.update(username, email, password, profileImage);
        User saved = userRepository.save(user);

        log.info("사용자 정보 수정 완료: userId = {}, username = {}", saved.getId(), saved.getUsername());

        return userMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.getUserDto.id")
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", userId)
                ));

        log.debug("사용자 삭제 요청: userId = {}, username = {}", user.getId(), user.getUsername());

        BinaryContent profile = user.getProfile();

        // 프로필 이미지 삭제(있는 경우)
        if(user.getProfile() != null) {
            binaryContentRepository.deleteById(profile.getId());
        }

        userRepository.deleteById(userId);

        log.info("사용자 삭제 완료: userId = {}", userId);
    }

    private void validateUsernameDuplicate(String username) {
        log.debug("username 중복 검증 시작");

        boolean exists = userRepository.existsByUsername(username);

        if (exists) {
            throw new UsernameAlreadyExistsException(
                    ErrorCode.USERNAME_ALREADY_EXISTS,
                    Map.of("username", username)
            );
        }
    }

    private void validateEmailDuplicate(String email) {
        log.debug("이메일 중복 검증 시작");

         boolean exists = userRepository.existsByEmail(email);

        if (exists) {
            throw new EmailAlreadyExistsException(
                    ErrorCode.EMAIL_ALREADY_EXISTS,
                    Map.of("email", email)
            );
        }
    }

    private BinaryContent saveProfileImage(BinaryContent oldProfile, CreateBinaryContentRequestDto request) {
        if (request == null) {
            return null;
        }

        log.debug("프로필 이미지 저장 시작");

        // 1. 기존 이미지 삭제
        if (oldProfile != null) {
            log.info("기존 프로필 이미지 삭제: {}", oldProfile.getFileName());
            binaryContentRepository.deleteById(oldProfile.getId());
        }

        // 2. 신규 이미지 저장
        BinaryContent newProfile = new BinaryContent(
                request.fileName(),
                request.size(),
                request.contentType()
        );

        BinaryContent saved = binaryContentRepository.save(newProfile);
        binaryContentStorage.put(saved.getId(), request.bytes());

        log.info("프로필 이미지 저장 완료: {}", saved.getFileName());

        return saved;
    }
}
