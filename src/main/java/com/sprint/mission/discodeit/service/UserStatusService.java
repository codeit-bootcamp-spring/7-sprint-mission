package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(CreateUserStatusDto createUserStatusDto);

    UserStatus getUserStatus(UUID userStatusId);

    List<UserStatus> getAllUserStatuses();

    void updateUserStatus(UpdateUserStatusDto updateUserStatusDto);

    void deleteById(UUID userStatusId);
}
