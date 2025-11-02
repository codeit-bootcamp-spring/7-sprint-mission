package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.UserProfileImageUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserResponseDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserNameUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPasswordUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPhoneNumUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserStateUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    // 생성
    UserResponseDto createUser(UserRequestDto requestDto);

    // 조회
    Optional<UserResponseDto> findUserInfoById(UUID userId);
    Optional<User> findUserEntityById(UUID userID);
    List<UserResponseDto> findAllUsers();

    Optional<UserResponseDto> findUserInfoByEmail(String email);

    // 수정
    Optional<UserResponseDto> updateUserName(UserNameUpdateDto updateDto);
    Optional<UserResponseDto> changePassword(UserPasswordUpdateDto updateDto);
    Optional<UserResponseDto> updateState(UserStateUpdateDto updateDto);
    Optional<UserResponseDto> updatePhoneNum(UserPhoneNumUpdateDto updateDto);

    // 프로필 이미지 변경
    Optional<UserResponseDto> updateProfileImage(UserProfileImageUpdateDto userProfileImageUpdateDto);

    // 삭제
    boolean deleteUser(UUID userId); // 삭제 메서드 추가 (성공 여부 반환)
}