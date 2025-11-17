package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatus createReadStatus(ReadStatusCreateRequest requestDto);

    ReadStatusDto findReadStatusById(UUID id);

    List<ReadStatusDto> findAllByUserId(UUID userId);

    ReadStatus updateReadStatus(UUID id, ReadStatusUpdateRequest updateDto);

    void deleteReadStatus(UUID id);


}
