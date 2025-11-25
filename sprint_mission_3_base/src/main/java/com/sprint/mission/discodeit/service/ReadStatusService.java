package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusDto create(ReadStatusCreateRequest request);

    ReadStatusDto find(UUID id);

    List<ReadStatusDto> findAllByUserId(UUID userId);

    void delete(UUID id);
}
