package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.update.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatus createReadStatus(CreateReadStatusRequestDto request);
    ReadStatus find(UUID readStatusId);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus updateReadStatus(UUID readStatusId, UpdateReadStatusDto updateReadStatus);
    void deleteReadStatus(UUID readStatusId);
}
