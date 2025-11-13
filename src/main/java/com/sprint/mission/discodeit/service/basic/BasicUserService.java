package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    // 고도화 의존성 추가
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    // 생성
    @Override
    public User createUser(UserCreateRequest requestDto, MultipartFile profileImage) {
        userRepository.findByEmail(requestDto.email()).ifPresent(user
                -> {
            throw new DuplicateEmailException("이미 존재하는 이메일");
        });

        userRepository.findByUserName(requestDto.username()).ifPresent(user
                -> {
            throw new DuplicateEmailException("이미 존재하는 닉네임");
        });

        UUID profileId = saveProfileImage(profileImage);

        // 유저 생성
        User newUser = User.builder()
                .email(requestDto.email())
                .username(requestDto.username())
                .password(requestDto.password())
                .build();

        newUser.updateProfileId(profileId);
        userRepository.save(newUser);
        userStatusRepository.save(new UserStatus(newUser.getId()));

        return newUser;
    }

    private UUID saveProfileImage(MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty()) {
            return null;
        }
        try {
            BinaryContent binaryContent = BinaryContent.builder()
                    .binaryData(profileImage.getBytes())
                    .dataName(profileImage.getOriginalFilename())
                    .dataType(profileImage.getContentType())
                    .build();
            binaryContentRepository.save(binaryContent);
            return binaryContent.getId();

        } catch (IOException e) {
            throw new RuntimeException("오류가 발생", e);
        }
    }

    private UserDto toDto(User user) {

        boolean isOnline = userStatusRepository.findStatusByUserId(user.getId())
                .map(UserStatus::isOnline).orElse(false);
        return UserDto.from(user, isOnline);
    }

    // --- 조회 ---

    // ID로 출력
    @Override
    public UserDto findUserById(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));
        return toDto(user);
    }

    // 전체출력
    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // --- 수정 ---

    @Override
    public User updateUserInfo(UUID userId, UserUpdateRequest updateDto, MultipartFile profileImage) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        if (profileImage != null && !profileImage.isEmpty()) {
            UUID oldProfileId = user.getProfileId();
            user.updateProfileId(saveProfileImage(profileImage));
            if (oldProfileId != null) {
                binaryContentRepository.deleteById(oldProfileId);
            }
        }

        if (updateDto.newUsername() != null && !updateDto.newUsername().isBlank()) {
            userRepository.findByUserName(updateDto.newUsername()).ifPresent(existingUser -> {
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
            user.updateEmail(updateDto.newUsername());
        }

        if (updateDto.newPassword() != null && !updateDto.newPassword().isBlank()) {
            user.updatePassword(updateDto.newPassword());
        }

        userRepository.save(user);
        return user;
    }


    // 논리 삭제
    @Override
    public void deleteUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        // 상태 삭제
        userStatusRepository.deleteStatusByUserId(userId);
        // 이미지 삭제
        if (user.getProfileId() != null)
            binaryContentRepository.deleteById(user.getProfileId());

        user.softDelete(); // 논리 삭제
        userRepository.save(user);    // 유저만 저장해서, 재시작 시 채널이나 메시지에는 적용이 안됨;;
    }
}