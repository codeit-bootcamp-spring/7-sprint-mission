package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequestDto readStatusCreateRequestDto);
    ReadStatus get(UUID id);
    List<ReadStatus> getAllByUserId(UUID userId);
    ReadStatus update(ReadStatusUpdateRequestDto readStatusUpdateRequestDto);
    boolean delete(UUID id);
}
