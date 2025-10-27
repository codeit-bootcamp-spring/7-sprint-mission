package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto getById(String id);
    List<UserResponseDto> getAllUsers();
    List<UserResponseDto> getOnlineUsers();

    void signIn(UserCreateRequestDto dto);
    UserResponseDto login(String id, String passwd);
    void update(UserUpdateRequestDto dto);

    void deleteById(String id);

}
