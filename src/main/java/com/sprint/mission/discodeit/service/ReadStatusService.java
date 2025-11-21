package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.readStatus.ReadStatusDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    public ReadStatusDto createReadStatus(ReadStatusCreateRequestDto readStatusCreateRequestDto);
    public void deleteReadStatus(UUID readStatusID);
    public List<ReadStatusDto> findAllyByUserId(UUID userID);
    public void resetReadStatus();
    public List<ReadStatusDto> readAllReadStatus();
    ReadStatusDto patchReadStatus(UUID readStatusId, ReadStatusPatchRequestDto readStatusPatchRequestDto);
}
