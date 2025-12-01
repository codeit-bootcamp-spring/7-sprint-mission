package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface UserService {
    UserResponseDto create(UserCreateRequestDto userCreateRequestDto, BinaryContentCreateRequestDto binaryContentCreateRequestDto);
    default UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        return create(userCreateRequestDto, null);
    }
    UserResponseDto get(UUID userId);
    List<UserResponseDto> getAll();
    UserResponseDto update(UUID userId, UserUpdateRequestDto userUpdateRequestDto, BinaryContentCreateRequestDto binaryContentCreateRequestDto);
    default UserResponseDto update(UUID userId, UserUpdateRequestDto userUpdateRequestDto) {
        return update(userId, userUpdateRequestDto, null);
    }

    boolean delete(UUID userid);
    List<UserResponseDto> getUsersByName(String username);
    Optional<UserResponseDto> getUsersByEmail(String email);
    void login(UUID userId);
    void logout(UUID userId);
    boolean isOnline(UUID userId);
}
