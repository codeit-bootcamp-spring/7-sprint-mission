package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponseDto create(CreateReadStatusRequestDto request);
    ReadStatus find(UUID readStatusId);
    List<ReadStatusResponseDto> findAllByUserId(UUID userId);
    ReadStatusResponseDto update(UUID readStatusId, UpdateReadStatusRequestDto request);
    void delete(UUID readStatusId);
}
