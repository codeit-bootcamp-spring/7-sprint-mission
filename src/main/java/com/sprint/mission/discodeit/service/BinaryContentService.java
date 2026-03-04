package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.enums.BinaryContentStatus;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentDto createBinaryContent(BinaryContentCreateRequest requestDto);
    BinaryContentDto findBinaryContentById(UUID id);
    List<BinaryContentDto> findAllBinaryContentByIdIn(List<UUID> id);
    BinaryContentDto updateStatus(UUID id, BinaryContentStatus status);
    void deleteBinaryContentById(UUID id);



}
