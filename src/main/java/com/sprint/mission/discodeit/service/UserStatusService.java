package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    void createUserStatus(CreateUserStatusRequestDto request);
    UserStatus findUserStatus(UUID id);
    List<UserStatus> findAllUserStatus();
    void updateUserStatus(UpdateUserStatusRequestDto request);
    void updateByUserId(UUID userId);
    void deleteUserStatus(UUID id);
}
