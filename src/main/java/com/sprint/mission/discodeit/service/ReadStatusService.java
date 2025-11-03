package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus createReadStatus(CreateReadStatusDto createReadStatusDto);

    ReadStatus getReadStatus(UUID readStatusId);

    List<ReadStatus> getAllReadStatusByUserId(UUID userId);

    void updateReadStatus(UpdateReadStatusDto updateReadStatusDto);

    void deleteReadStatus(UUID readStatusId);


}
