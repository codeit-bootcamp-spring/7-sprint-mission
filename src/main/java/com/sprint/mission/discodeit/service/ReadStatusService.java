package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void create(CreateReadStatusRequestDto request);
    ReadStatus find(UUID readStatusId);
    List<ReadStatus> findAllByUserId(UUID userId);
    void update(UpdateReadStatusRequestDto request);
    void delete(UUID readStatusId);
}
