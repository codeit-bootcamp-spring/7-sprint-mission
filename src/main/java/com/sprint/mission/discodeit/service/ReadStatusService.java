package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponseDto createReadStatus(CreateReadStatusDto createReadStatusDto);

    ReadStatusResponseDto getReadStatus(UUID readStatusId);

    List<ReadStatusResponseDto> getAllReadStatusByUserId(UUID userId);

    ReadStatusResponseDto updateReadStatus(UUID readStatusId, UpdateReadStatusDto updateReadStatusDto);

    void deleteReadStatus(UUID readStatusId);


}
