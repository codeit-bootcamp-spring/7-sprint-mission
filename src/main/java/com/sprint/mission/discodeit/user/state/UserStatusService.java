package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.common.service.BaseService;
import com.sprint.mission.discodeit.config.enums.Status;

import java.util.List;
import java.util.UUID;

public interface UserStatusService extends BaseService<UserStatus, UUID> {
    UserStatusDTO create(UUID userId);

    UserStatusDTO toAway(UUID userId);
    UserStatusDTO toOffline(UUID userId);
    UserStatusDTO toOnline(UUID userId);
    UserStatusDTO toDoNotDisturb(UUID userId, String message);

    UserStatusDTO findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    void deleteByUserId(UUID userId);

    List<UserStatusDTO> findAllByState(Status currentStatus);


}
