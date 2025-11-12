package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatus createReadStatus(ReadStatusRequestDto requestDto);

    ReadStatusResponseDto findReadStatusById(UUID id);

    List<ReadStatusResponseDto> findAllByUserId(UUID userId);

    ReadStatus updateReadStatus(UUID id, ReadStatusUpdateDto updateDto);

    void deleteReadStatus(UUID id);


}
