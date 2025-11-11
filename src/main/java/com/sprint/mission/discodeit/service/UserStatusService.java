package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequestDto userStatusCreateRequestDto);
    UserStatus update(UserStatusUpdateRequestDto userStatusUpdateRequestDto);
    UserStatus updateByUserId(UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto);
    UserStatus get(UUID id);
    List<UserStatus> getAll();
    boolean delete(UUID id);
}
