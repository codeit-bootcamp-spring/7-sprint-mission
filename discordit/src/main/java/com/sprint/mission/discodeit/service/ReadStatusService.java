package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    public ReadStatusResponse create(ReadStatusCreateRequest dto);
    public void delete(UUID uuid);

    public ReadStatusResponse get(UUID uuid);
    List<ReadStatusResponse> getAllByUserId(String userId);

    public void read(UUID uuid);
}
