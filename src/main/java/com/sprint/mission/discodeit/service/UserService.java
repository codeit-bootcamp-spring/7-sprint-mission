package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.binaryContentDto.UserProfileImageUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserResponseDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserNameUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPasswordUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPhoneNumUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserStateUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // 생성
    UserResponseDto createUser(UserRequestDto requestDto);

    // 조회
    UserResponseDto findUserInfoById(UUID userId);
    List<UserResponseDto> findAllUsers();

    UserResponseDto findUserInfoByEmail(String email);

    // 수정
    UserResponseDto updateUserName(UserNameUpdateDto updateDto);
    UserResponseDto updatePassword(UserPasswordUpdateDto updateDto);
    UserResponseDto updateState(UserStateUpdateDto updateDto);
    UserResponseDto updatePhoneNum(UserPhoneNumUpdateDto updateDto);

    // 프로필 이미지 변경
    UserResponseDto updateProfileImage(UserProfileImageUpdateDto userProfileImageUpdateDto);

    // 삭제
    void deleteUser(UUID userId); // 삭제 메서드 추가 (성공 여부 반환)
}