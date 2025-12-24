package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.entity.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.entity.readStatus.request.ReadStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(ReadStatusCreateRequest dto);

    ReadStatusDto update(UUID id, ReadStatusUpdateRequest dto);

    ReadStatusDto get(UUID uuid);

    List<ReadStatusDto> getAllByUserId(UUID id);

    void delete(UUID uuid);
}
