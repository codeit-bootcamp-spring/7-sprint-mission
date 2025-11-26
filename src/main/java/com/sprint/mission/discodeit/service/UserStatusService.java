package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatustUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UserStatusCreateRequest request);

    UserStatusDto find(UUID userStatusId);

    List<UserStatusDto> findAll();

    UserStatusDto update(UUID userId, UserStatusUpdateRequest request);

    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

    void delete(UUID userStatusId);

}
