package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.binaryContent.FileOperationFailedException;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;
    private final PasswordEncoder passwordEncoder;

    // 생성
    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest requestDto, MultipartFile profileImage) {

        // email, 닉네임 공백 제거
        String email = requestDto.email().replace(" ", "");
        String username = requestDto.username().trim();

        log.debug("회원가입 요청 - email: {}, username: {}", email, username);
        userRepository.findByEmail(email).ifPresent(user
                -> {
            log.warn("회원가입 실패 - 중복된 이메일: {}", email);
            throw new DuplicateEmailException(email); });

        userRepository.findByUsername(username).ifPresent(user
                -> {
            log.warn("회원가입 실패 - 중복된 유저이름: {}", username);
            throw new DuplicateNameException(username); });

        BinaryContent profile = saveProfileImage(profileImage);

        String encodedPassword = passwordEncoder.encode(requestDto.password());

        // 유저 생성
        User newUser = User.builder()
                .email(email)
                .username(username)
                .password(encodedPassword)
                .build();

        newUser.updateProfile(profile);
        UserStatus status = new UserStatus(newUser);
        newUser.updateStatus(status);

        userRepository.save(newUser);
        log.info("회원가입 완료 - userId: {}", newUser.getId());
        return userMapper.toDto(newUser);
    }

    private BinaryContent saveProfileImage(MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty()) {
            return null;
        }
        BinaryContent binaryContent = BinaryContent.builder()
                .fileName(profileImage.getOriginalFilename())
                .size(profileImage.getSize())
                .contentType(profileImage.getContentType())
                .build();
        binaryContent = binaryContentRepository.save(binaryContent);

        try {
            binaryContentStorage.put(binaryContent.getId(), profileImage.getBytes());
        } catch (IOException e) {
            log.debug("이미지 업로드 실패: {}", binaryContent.getId());
            throw new FileOperationFailedException(binaryContent.getId());
        }
        return binaryContent;
    }

    // --- 조회 ---

    // ID로 출력
    @Override
    @Transactional(readOnly = true)
    public UserDto findUserById(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return userMapper.toDto(user);
    }

    // 전체출력
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return userRepository.findAllWithProfileAndStatus().stream()
                .map(userMapper::toDto).collect(Collectors.toList());
    }

    // --- 수정 ---

    @Override
    @Transactional
    public UserDto updateUserInfo(UUID userId, UserUpdateRequest updateDto, MultipartFile profileImage) {

        log.debug("회원 정보 수정 요청 - userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("유저 수정 실패 - 존재하지 않는 유저: {}", userId);
                    return new UserNotFoundException(userId);
                });

        if (profileImage != null && !profileImage.isEmpty()) {
            // 기존 프로필사진 확인
            BinaryContent oldProfile = user.getProfile();
            UUID oldProfileId = oldProfile != null ? user.getProfile().getId() : null;
            // 업데이트
            user.updateProfile(saveProfileImage(profileImage));
            // 기존 프로필 삭제
            if (oldProfileId != null) {
                binaryContentRepository.deleteById(oldProfileId);
            }
        }

        if (updateDto.newUsername() != null && !updateDto.newUsername().isBlank()) {
            String newUsername = updateDto.newUsername().trim();

            userRepository.findByUsername(newUsername).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(user.getId())) {
                    log.warn("유저 수정 실패 - 중복된 유저이름: {}", newUsername);
                    throw new DuplicateNameException(newUsername);
                }
            });
            user.updateUsername(newUsername);
        }

        if (updateDto.newEmail() != null && !updateDto.newEmail().isBlank()) {
            String newEmail = updateDto.newEmail().replace(" ", "");

            userRepository.findByEmail(newEmail).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(user.getId())) {
                    log.warn("유저 수정 실패 - 중복된 이메일: {}", newEmail);
                    throw new DuplicateEmailException(newEmail);
                }
            });
            user.updateEmail(newEmail);
        }

        if (updateDto.newPassword() != null && !updateDto.newPassword().isBlank()) {
            user.updatePassword(updateDto.newPassword());
        }

        userRepository.save(user);
        log.info("유저 정보 수정 완료: {}", user.getId());
        return userMapper.toDto(user);
    }


    // 삭제
    @Override
    @Transactional
    public void deleteUser(UUID userId) {

        log.info("유저 삭제 요청: {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("유저 삭제 실패 - 존재하지 않는 유저: {}", userId);
                    return new UserNotFoundException(userId);
                });

        log.info("유저 삭제 성공: {}", userId);
        userRepository.deleteById(userId);
    }
}