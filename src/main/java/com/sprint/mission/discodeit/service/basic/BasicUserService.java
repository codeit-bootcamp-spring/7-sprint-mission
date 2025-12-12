package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
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
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService{

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserMapper userMapper;

    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public UserResponseDto create(CreateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest) {

        log.debug("신규 사용자 생성 요청: username = {}, email = {}",
                userRequest.username(), userRequest.email());

        // username, email 중복 검사
        validateUsernameDuplicate(userRequest.username());
        validateEmailDuplicate(userRequest.email());

        // 이메일, 비밀번호 포맷 검사
        validateEmailFormat(userRequest.email());
        validatePasswordFormat(userRequest.password());

        // 선택적 프로필 이미지 처리
        BinaryContent profileImage = saveProfileImage(null, profileRequest);

        // 사용자 생성
        User newUser  = new User(
                userRequest.username(),
                userRequest.email(),
                userRequest.password(),
                profileImage
        );

        UserStatus userStatus = new UserStatus(newUser);
        newUser.setStatus(userStatus);

        // 사용자 저장
        User saved = userRepository.save(newUser);
        userStatusRepository.save(userStatus);

        log.info("새 유저 생성 완료: userId = {}, username = {}, email = {}",
                saved.getId(), saved.getUsername(), saved.getEmail());

        return userMapper.toResponseDto(newUser);
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        // fetch join 적용
        return userRepository.findAllWithProfileAndStatus().stream()
                .map(u -> userMapper.toResponseDto(u))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto update(UUID userId, UpdateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        log.debug("사용자 정보 수정 요청: userId = {}, username = {}", user.getId(), user.getUsername());

        String username = null;
        String email = null;
        String password = null;

        // 프로필 이미지만 전달된 경우 null 참조 방지
        if (Optional.ofNullable(userRequest).isPresent()) {
            username = userRequest.newUsername();
            email = userRequest.newEmail();
            password = userRequest.newPassword();

            // username, email 중복 검사
            validateUsernameDuplicate(username);
            validateEmailDuplicate(email);

            // 이메일, 비밀번호 포맷 검사
            validateEmailFormat(email);
            validatePasswordFormat(password);
        }

        // 선택적 프로필 이미지 처리
        BinaryContent profileImage = saveProfileImage(user.getProfile(), profileRequest);

        user.update(username, email, password, profileImage);
        userRepository.save(user);

        log.info("사용자 정보 수정 완료: userId = {}, username = {}", user.getId(), user.getUsername());

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        log.debug("사용자 삭제 요청: userId = {}, username = {}", user.getId(), user.getUsername());

        BinaryContent profile = user.getProfile();
        UserStatus userStatus = user.getStatus();

        // 프로필 이미지 삭제(있는 경우)
        if(user.getProfile() != null) {
            binaryContentRepository.deleteById(profile.getId());
        }

        userStatusRepository.deleteById(userStatus.getId());
        userRepository.deleteById(id);

        log.info("사용자 삭제 완료");
    }

    private void validateUsernameDuplicate(String username) {
        log.debug("username 중복 검증 시작");

        boolean exists = userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(username));

        if (exists) {
            log.warn("username {}은 이미 존재합니다.", username);
            throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }

    private void validateEmailDuplicate(String email) {
        log.debug("이메일 중복 검증 시작");

        boolean exists = userRepository.findAll().stream().
                anyMatch(u -> u.getEmail().equals(email));

        if (exists) {
            log.warn("email {}은 이미 존재합니다.", email);
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateEmailFormat(String email) {
        Optional.ofNullable(email).ifPresent(e ->{
            log.debug("이메일 형식 검증 시작: {}", e);
            UserValidator.validateEmail(e);
        });
    }

    private void validatePasswordFormat(String password) {
        Optional.ofNullable(password).ifPresent(pw -> {
            log.debug("패스워드 형식 검증 시작");
            UserValidator.validatePassword(pw);
        });
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

        binaryContentRepository.save(newProfile);
        binaryContentStorage.put(newProfile.getId(), request.bytes());

        log.info("프로필 이미지 저장 완료: {}", newProfile.getFileName());

        return newProfile;
    }
}
