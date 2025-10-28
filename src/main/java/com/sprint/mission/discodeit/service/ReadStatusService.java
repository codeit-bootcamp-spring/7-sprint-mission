package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    public ReadStatus createReadStatus(ReadStatusCreateRequestDto readStatusCreateRequestDto);
    public void deleteReadStatus(UUID readStatusID);
    public <T>void updateReadStatus(ReadStatusUpdateRequestDto<T> readStatusUpdateRequestDto);
    public ReadStatus readReadStatus(UUID readStatusID);
    public List<ReadStatus> findAllyByUserId(UUID userID);
    public void resetReadStatus();
}
