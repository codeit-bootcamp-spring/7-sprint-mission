package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;

    // 생성
    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest requestDto, MultipartFile profileImage) {
        userRepository.findByEmail(requestDto.email()).ifPresent(user
                -> { throw new DuplicateEmailException("이미 존재하는 이메일"); });

        userRepository.findByUsername(requestDto.username()).ifPresent(user
                -> { throw new DuplicateEmailException("이미 존재하는 닉네임"); });

        BinaryContent profile = saveProfileImage(profileImage);

        // 유저 생성
        User newUser = User.builder()
                .email(requestDto.email())
                .username(requestDto.username())
                .password(requestDto.password())
                .build();

        newUser.updateProfile(profile);
        UserStatus status = new UserStatus(newUser);
        newUser.updateStatus(status);

        userRepository.save(newUser);
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
            throw new RuntimeException("파일 저장 실패");
        }
        return binaryContent;
    }

    // --- 조회 ---

    // ID로 출력
    @Override
    public UserDto findUserById(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));
        return userMapper.toDto(user);
    }

    // 전체출력
    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAllWithProfile().stream()
                .map(userMapper::toDto).collect(Collectors.toList());
    }

    // --- 수정 ---

    @Override
    @Transactional
    public UserDto updateUserInfo(UUID userId, UserUpdateRequest updateDto, MultipartFile profileImage) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

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
            userRepository.findByUsername(updateDto.newUsername()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(user.getId())) {
                    throw new DuplicateEmailException("이미 존재하는 닉네임");
                }
            });
            user.updateUsername(updateDto.newUsername());
        }

        if (updateDto.newEmail() != null && !updateDto.newEmail().isBlank()) {
            userRepository.findByEmail(updateDto.newEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(user.getId())) {
                    throw new DuplicateEmailException("이미 존재하는 이메일");
                }
            });
            user.updateEmail(updateDto.newEmail());
        }

        if (updateDto.newPassword() != null && !updateDto.newPassword().isBlank()) {
            user.updatePassword(updateDto.newPassword());
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }


    // 삭제
    @Override
    @Transactional
    public void deleteUser(UUID userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        userRepository.deleteById(userId);
    }
}