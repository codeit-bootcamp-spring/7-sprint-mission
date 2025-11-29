package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.readstatus.ReadStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponseDto create(ReadStatusCreateRequestDto readStatusCreateRequestDto);
    ReadStatusResponseDto get(UUID id);
    List<ReadStatusResponseDto> getAllByUserId(UUID userId);
    ReadStatusResponseDto update(UUID id, ReadStatusUpdateRequestDto readStatusUpdateRequestDto);
    boolean delete(UUID id);
}
