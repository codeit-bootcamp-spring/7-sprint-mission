package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto getByUserId(String userId);
    UserResponseDto getById(UUID uuid);
    List<UserResponseDto> getAllUsers();
    List<UserResponseDto> getOnlineUsers();

    void signIn(UserCreateRequestDto dto);
    UserResponseDto login(String id, String passwd);
    void update(UserUpdateRequestDto dto);

    void deleteByUserId(String id);
    void deleteById(UUID uuid);

    // 콘솔 편의용 조회 API (필드 단위)
    String getDisplayName(String userId);
    String getBio(String userId);
    OnlineStatus getOnlineStatus(String userId);

}
