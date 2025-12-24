package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.entity.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.entity.userStatus.response.UserStatusUpdateResponse;

import java.util.UUID;

public interface UserStatusService {
    UserStatusUpdateResponse create(UserStatusCreateRequest dto);
    UserStatusUpdateResponse get(UUID id);
    UserStatusUpdateResponse updateByUser(UUID userId, UserStatusUpdateRequest request);
}
