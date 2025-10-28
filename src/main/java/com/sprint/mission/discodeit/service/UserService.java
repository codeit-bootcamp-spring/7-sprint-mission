package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.entity.binaryContent.dto.UserProfileImageRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserNameUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPasswordUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPhoneNumUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserStateUpdateDto;

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
    Optional<UserInfoDto> updateUserName(UserNameUpdateDto updateDto);
    Optional<UserInfoDto> changePassword(UserPasswordUpdateDto updateDto);
    Optional<UserInfoDto> updateState(UserStateUpdateDto updateDto);
    Optional<UserInfoDto> updatePhoneNum(UserPhoneNumUpdateDto updateDto);

    // 프로필 이미지 변경
    Optional<UserInfoDto> updateProfileImage(UserProfileImageRequestDto userProfileImageRequestDto);

    // 삭제
    boolean deleteUser(UUID userId); // 삭제 메서드 추가 (성공 여부 반환)
}