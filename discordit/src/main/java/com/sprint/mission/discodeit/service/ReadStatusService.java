package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponse create(ReadStatusCreateRequest dto);
    ReadStatusResponse update(ReadStatusUpdateRequest dto);

    void delete(UUID uuid);
    ReadStatusResponse get(UUID uuid);

    List<ReadStatusResponse> getAllByUserId(UUID id);

    List<ReadStatusResponse> getAll();
}
