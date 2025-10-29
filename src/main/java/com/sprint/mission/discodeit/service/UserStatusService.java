package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequest request);
    UserStatus find(UUID userStatusId);
    List<UserStatus> findAll();
    UserStatus update(UserStatustReUpdateRequest request);
    UserStatus updateByUserId(UUID userId);
    void delete(UUID userStatusId);

}
