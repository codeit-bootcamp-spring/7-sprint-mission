package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    UUID create(ReadStatusCreateRequest request);
    Optional<ReadStatus> find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    void update(ReadStatusUpdateRequest request);
    void delete(UUID id);
}
