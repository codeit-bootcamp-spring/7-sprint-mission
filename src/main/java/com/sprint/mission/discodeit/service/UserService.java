package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.CreateUserCommand;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.update.UpdateUserDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDto createUser(CreateUserCommand request); // 유저 생성
    UserResponseDto find(UUID id); //유저 조회
    List<UserResponseDto> findAll(); // 모든 유저 조회
    UserResponseDto updateUser(UUID id, UpdateUserCommand request);
    void deleteUser(UUID userId); // 유저 삭제
}
