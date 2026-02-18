package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userDto.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.role.Role;
import com.sprint.mission.discodeit.exception.binaryContent.FileOperationFailedException;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.JwtRegistry;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
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
    private final AuthService authService;
    private final JwtRegistry jwtRegistry;

    // 생성
    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest requestDto, MultipartFile profileImage) {

        // email, 닉네임 공백 제거
        String email = requestDto.email().replace(" ", "");
        String username = requestDto.username().trim();

        log.debug("회원가입 요청 - email: {}, username: {}", email, username);
        if (userRepository.existsByEmail(email)) {
            log.warn("회원가입 실패 - 중복된 이메일: {}", email);
            throw new DuplicateEmailException(email);
        }

        if (userRepository.existsByUsername(username)) {
            log.warn("회원가입 실패 - 중복된 유저이름: {}", username);
            throw new DuplicateNameException(username);
        }

        BinaryContent profile = saveProfileImage(profileImage);

        String encodedPassword = passwordEncoder.encode(requestDto.password());

        // 유저 생성
        User newUser = User.builder()
                .email(email)
                .username(username)
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        newUser.updateProfile(profile);

        userRepository.save(newUser);
        log.info("회원가입 완료 - userId: {}", newUser.getId());
        return userMapper.toDto(newUser, false);
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
        boolean isOnline = authService.isOnline(userId);
        return userMapper.toDto(user, isOnline);
    }

    // 전체출력
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {

        List<User> users = userRepository.findAll();
        Set<UUID> onlineUserIds = authService.getOnlineUserIds();

        return users.stream()
                .map(user -> userMapper.toDto(user, onlineUserIds.contains(user.getId())))
                .collect(Collectors.toList());
    }

    // --- 수정 ---

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public UserDto updateUserInfo(UUID userId, UserUpdateRequest updateDto, MultipartFile profileImage) {

        log.debug("회원 정보 수정 요청 - userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("유저 수정 실패 - 존재하지 않는 유저: {}", userId);
                    return new UserNotFoundException(userId);
                });

        boolean isChanged = false;

        if (profileImage != null && !profileImage.isEmpty()) {
            // 기존 프로필사진 확인
            BinaryContent oldProfile = user.getProfile();
            // 업데이트
            user.updateProfile(saveProfileImage(profileImage));
            // 기존 프로필 삭제
            if (oldProfile != null) {
                binaryContentRepository.deleteById(oldProfile.getId());
            }
            isChanged = true;
        }

        if (updateDto.newUsername() != null && !updateDto.newUsername().isBlank()) {
            String newUsername = updateDto.newUsername().trim();

            if (!user.getUsername().equals(newUsername)) {
                if (userRepository.existsByUsername(newUsername)) {
                    log.warn("유저 수정 실패 - 중복된 유저이름: {}", newUsername);
                    throw new DuplicateNameException(newUsername);
                }

                user.updateUsername(newUsername);
                isChanged = true;
            }
        }

        if (updateDto.newEmail() != null && !updateDto.newEmail().isBlank()) {
            String newEmail = updateDto.newEmail().replace(" ", "");

            if (!user.getEmail().equals(newEmail)) {
                if (userRepository.existsByEmail(newEmail)) {
                    log.warn("유저 수정 실패 - 중복된 이메일: {}", newEmail);
                    throw new DuplicateEmailException(newEmail);
                }

                user.updateEmail(newEmail);
                isChanged = true;
            }
        }

        if (updateDto.newPassword() != null && !updateDto.newPassword().isBlank()) {

            if (!passwordEncoder.matches(updateDto.newPassword(), user.getPassword())) {
                String newPassword = passwordEncoder.encode(updateDto.newPassword());
                user.updatePassword(newPassword);
                isChanged = true;
            }
        }

        if (isChanged) {
            userRepository.save(user);
            log.info("유저 정보 수정 완료: {}", user.getId());
        }
        boolean isOnline = authService.isOnline(userId);
        return userMapper.toDto(user, isOnline);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateUserRole(RoleUpdateRequest roleUpdateRequest) {

        User user = userRepository.findById(roleUpdateRequest.userId())
                .orElseThrow(() -> new UserNotFoundException(roleUpdateRequest.userId()));

        Role oldRole = user.getRole();

        user.updateRole(roleUpdateRequest.newRole());
        userRepository.save(user);
        jwtRegistry.invalidateJwtInformationByUserId(user.getId());

        log.info("유저 {} 권한 변경: {} -> {}", user.getUsername(), oldRole, roleUpdateRequest.newRole());
        boolean isOnline = authService.isOnline(user.getId());

        return userMapper.toDto(user, isOnline);
    }

    // 삭제
    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
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