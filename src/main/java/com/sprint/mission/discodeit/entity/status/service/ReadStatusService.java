package com.sprint.mission.discodeit.entity.status.service;

import com.sprint.mission.discodeit.entity.status.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.entity.status.dto.ReadStatusInfoDto;
import com.sprint.mission.discodeit.entity.status.dto.ReadStatusUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusInfoDto createReadStatus(ReadStatusCreateDto createDto);

    Optional<ReadStatusInfoDto> findReadStatusById(UUID id);

    List<ReadStatusInfoDto> findAllByUserId(UUID userId);

    Optional<ReadStatusInfoDto> updateReadStatus(ReadStatusUpdateDto updateDto);

    void deleteReadStatus(UUID id);


}
