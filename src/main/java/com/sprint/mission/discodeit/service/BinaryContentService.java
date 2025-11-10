package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentResponseDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResponseDto createBinaryContent(BinaryContentRequestDto requestDto);
    BinaryContentResponseDto findBinaryContentById(UUID id);
    List<BinaryContentResponseDto> findAllBinaryContentByIdIn(List<UUID> id);
    void deleteBinaryContentById(UUID id);



}
