package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponseDto createUserStatus(CreateUserStatusDto createUserStatusDto);

    UserStatusResponseDto getUserStatus(UUID userStatusId);

    List<UserStatusResponseDto> getAllUserStatuses();

    UserStatusResponseDto updateUserStatus(UUID userId, UpdateUserStatusDto updateUserStatusDto);

    UserStatusResponseDto updateStatusByUserId(UUID userId, UpdateUserStatusDto updateUserStatusDto);

    void deleteById(UUID userStatusId);
}
