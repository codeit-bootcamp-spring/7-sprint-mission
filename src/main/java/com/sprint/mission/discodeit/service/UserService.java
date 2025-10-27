package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/*
    controller가 없어서 임시로 반환타입 변경
 */
// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface UserService {
    UserResponseDto create(UserCreateRequestDto userCreateRequestDto);
    UserResponseDto get(UUID uuid);
    List<UserResponseDto> getAll();
    UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto);
    boolean delete(UUID userid);
    List<User> getUsersByName(String username);
    Optional<User> getUsersByEmail(String email);
    List<User> getUsersByState(UserState userState);
    void login(UUID userId);
    void logout(UUID userId);
}
