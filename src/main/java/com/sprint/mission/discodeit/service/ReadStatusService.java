package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusResponseDto createReadStatus(UUID channelId, CreateReadStatusRequestDto request);
    ReadStatusResponseDto find(UUID userId);
    List<ReadStatusResponseDto> findAllByUserId(UUID userId);
    ReadStatusResponseDto updateReadStatus(UUID channelId, UpdateReadStatusDto updateReadStatus);
    void deleteReadStatus(UUID readStatusId);
}
