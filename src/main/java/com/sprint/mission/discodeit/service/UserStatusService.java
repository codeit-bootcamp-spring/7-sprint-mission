package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(CreateUserStatusDto createUserStatusDto);

    UserStatus getUserStatus(UUID userStatusId);

    List<UserStatus> getAllUserStatuses();

    void updateUserStatus(UUID userId, UpdateUserStatusDto updateUserStatusDto);

    UserStatus updateStatusByUserId(UUID userId, UpdateUserStatusDto updateUserStatusDto);

    void deleteById(UUID userStatusId);
}
