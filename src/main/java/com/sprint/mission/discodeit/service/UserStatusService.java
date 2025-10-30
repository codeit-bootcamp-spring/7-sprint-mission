package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.update.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatus createUserStatus(CreateUserStatusRequestDto requestDto);
    UserStatus find(UUID userStatusId);
    List<UserStatus> findAll();
    UserStatus updateUserStatus(UUID userStatusId, UpdateUserStatusDto updateDto);
    UserStatus updateByUserId(UUID userId, UpdateUserStatusDto updateDto);
    void deleteUserStatus(UUID userStatusId);
}
