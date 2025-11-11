package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.common.service.BaseService;
import com.sprint.mission.discodeit.config.enums.Status;

import java.util.List;
import java.util.UUID;

public interface UserStatusService extends BaseService<UserStatus, UUID> {
    UserStatusResponseDTO create(UUID userId);

    UserStatusResponseDTO toAway(UUID userId);
    UserStatusResponseDTO toOffline(UUID userId);
    UserStatusResponseDTO toOnline(UUID userId);
    UserStatusResponseDTO toDoNotDisturb(UUID userId, String message);

    UserStatusResponseDTO findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    void deleteByUserId(UUID userId);

    List<UserStatusResponseDTO> findAllByState(Status currentStatus);


}
