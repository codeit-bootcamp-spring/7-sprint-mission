package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void createReadStatus(CreateReadStatusRequestDto request);
    ReadStatus findReadStatus(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    void updateReadStatus(UpdateReadStatusRequestDto request);
    void deleteReadStatus(UUID id);
}
