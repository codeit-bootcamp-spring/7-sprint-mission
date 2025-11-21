package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    public ReadStatusResponseDto createReadStatus(ReadStatusCreateRequestDto readStatusCreateRequestDto);
    public void deleteReadStatus(UUID readStatusID);
    public List<ReadStatusResponseDto> findAllyByUserId(UUID userID);
    public void resetReadStatus();
    public List<ReadStatusResponseDto> readAllReadStatus();
    ReadStatusResponseDto patchReadStatus(UUID readStatusId, ReadStatusPatchRequestDto readStatusPatchRequestDto);
}
