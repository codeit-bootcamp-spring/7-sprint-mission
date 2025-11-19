package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    void create(CreateUserStatusRequestDto request);
    UserStatus find(UUID userStatusId);
    List<UserStatus> findAll();
    UserStatus update(UUID userStatusId, UpdateUserStatusRequestDto request);
    UserStatus updateByUserId(UUID userId, UpdateUserStatusRequestDto request);
    void delete(UUID userStatusId);
}
