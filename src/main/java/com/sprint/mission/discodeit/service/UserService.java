package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.entity.binaryContent.dto.UserProfileImageRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    // 생성
    UserInfoDto createUser(UserCreateRequestDto userCreateDto);

    // 조회
    Optional<UserInfoDto> findUserInfoById(UUID userId);
    Optional<User> findUserEntityById(UUID userID);
    List<UserInfoDto> findAllUsers();

    Optional<UserInfoDto> findUserInfoByEmail(String email);

    // 수정
    Optional<UserInfoDto> updateProfile(UUID userId, String newUserName, String newPhoneNum);
    Optional<UserInfoDto> changePassword(UUID userId, String newPassword);
    Optional<UserInfoDto> updateState(UUID userId, UserState newState);

    // 프로필 이미지 변경
    Optional<UserInfoDto> updateProfileImage(UserProfileImageRequestDto userProfileImageRequestDto);

    // 삭제
    boolean deleteUser(UUID userId); // 삭제 메서드 추가 (성공 여부 반환)
}