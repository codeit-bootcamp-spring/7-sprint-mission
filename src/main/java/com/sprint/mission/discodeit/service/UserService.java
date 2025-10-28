package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.request.ProfileRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(CreateUserRequestDto userRequestDto,
                    ProfileRequestDto profileRequestDto); // 유저 생성

    UserResponseDto find(UUID id); //유저 조회
    List<UserResponseDto> findAll(); // 모든 유저 조회
    UserResponseDto updateUser(UUID id, UpdateUserDto  updateUserDto, ProfileRequestDto profileRequestDto);
    void deleteUser(UUID userId); // 유저 삭제
}
