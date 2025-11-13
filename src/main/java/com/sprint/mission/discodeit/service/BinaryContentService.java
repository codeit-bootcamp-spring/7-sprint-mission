package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentDto createBinaryContent(BinaryContentCreateRequest requestDto);
    BinaryContentDto findBinaryContentById(UUID id);
    List<BinaryContentDto> findAllBinaryContentByIdIn(List<UUID> id);
    void deleteBinaryContentById(UUID id);



}
