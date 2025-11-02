package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateByIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponse create(UserStatusCreateRequest dto);

    UserStatusResponse get(UUID id);
    List<UserStatusResponse> getAll();

    void update(UserStatusUpdateByIdRequest dto);
    void updateById(UserStatusUpdateByUserIdRequest dto);

    void delete(UUID id);

}
