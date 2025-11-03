package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.UserProfileImageUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserResponseDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserNameUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPasswordUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPhoneNumUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserStateUpdateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public UserResponseDto createUser(UserRequestDto requestDto) {
        userRepository.findByEmail(requestDto.getEmail()).ifPresent(user
                -> {
            throw new DuplicateEmailException("이미 존재하는 이메일");
        });

        userRepository.findByUserName(requestDto.getUserName()).ifPresent(user
                -> {
            throw new DuplicateEmailException("이미 존재하는 닉네임");
        });

        User newUser = User.builder()
                .email(requestDto.getEmail())
                .userName(requestDto.getUserName())
                .password(requestDto.getPassword())
                .phoneNum(requestDto.getPhoneNum())
                .build();

        userRepository.save(newUser);

        UserStatus newUserStatus = new UserStatus(newUser.getId());
        userStatusRepository.save(newUserStatus);

        // 이미지 추가
        if (requestDto.getProfileImage() != null) {
            BinaryContent newImage = new BinaryContent(
                    newUser.getId(),
                    requestDto.getProfileImage(),
                    requestDto.getProfileName(),
                    requestDto.getProfileType()
                    // messageId = null;
            );
            binaryContentRepository.save(newImage);
        }


        return UserResponseDto.from(newUser, true);
    }

    // User -> UserInfoDto
    private UserResponseDto toDto(User user) {

        boolean isOnline = userStatusRepository.findStatusByUserId(user.getId())
                .map(UserStatus::isOnline).orElse(false);
        return UserResponseDto.from(user, isOnline);
    }

    // --- 조회 ---

    // ID로 출력
    @Override
    public UserResponseDto findUserInfoById(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.from(user, true);
    }

    // 이메일로 출력
    @Override
    public UserResponseDto findUserInfoByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.from(user, true);
    }

    // 전체출력
    @Override
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // --- 수정 ---
    @Override
    public UserResponseDto updateUserName(UserNameUpdateDto updateDto) {

        userRepository.findByUserName(updateDto.newUserName()).ifPresent(user -> {
            if (!user.getUserName().equals(updateDto.newUserName()))
                throw new DuplicateEmailException("이미 사용중인 닉네임입니다.");
        });

        User user = userRepository.findById(updateDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        user.updateName(updateDto.newUserName());
        userRepository.save(user);
        return toDto(user);
    }

    @Override
    public UserResponseDto changePassword(UserPasswordUpdateDto updateDto) {

        User user = userRepository.findById(updateDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        user.updatePassword(updateDto.newPassword());
        userRepository.save(user);
        return toDto(user);
    }

    @Override
    public UserResponseDto updateState(UserStateUpdateDto updateDto) {

        User user = userRepository.findById(updateDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        user.updateState(updateDto.newState());
        userRepository.save(user);
        return toDto(user);
    }

    @Override
    public UserResponseDto updatePhoneNum(UserPhoneNumUpdateDto updateDto) {

        User user = userRepository.findById(updateDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        user.updatePhoneNum(updateDto.phoneNum());
        userRepository.save(user);
        return toDto(user);
    }

    @Override
    public UserResponseDto updateProfileImage(UserProfileImageUpdateDto imageUpdateDto) {

        User user = userRepository.findById(imageUpdateDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        // 기존 이미지 삭제
        binaryContentRepository.deleteProfileImageByUserId(user.getId());
        // 새로운 이미지 업데이트
        if (imageUpdateDto.image() != null) {
            BinaryContent newImage = new BinaryContent(
                    user.getId(),
                    imageUpdateDto.image(),
                    imageUpdateDto.imageName(),
                    imageUpdateDto.imageType()
            );
            binaryContentRepository.save(newImage);
        }
        return toDto(user);

    }

    // 논리 삭제
    @Override
    public boolean deleteUser(UUID userId) {

        return userRepository.findById(userId).map(userDelete -> {
            if (channelRepository.existsByAdminId(userId)) {
                throw new IllegalStateException("채널관리자는 삭제할 수 없습니다.");
            }

            // 상태 삭제
            userStatusRepository.deleteStatusByUserId(userId);
            // 이미지 삭제
            binaryContentRepository.deleteProfileImageByUserId(userId);

            userDelete.softDelete(); // 논리 삭제
            userRepository.save(userDelete);    // 유저만 저장해서, 재시작 시 채널이나 메시지에는 적용이 안됨;;
            return true;
        }).orElse(false);
    }
}