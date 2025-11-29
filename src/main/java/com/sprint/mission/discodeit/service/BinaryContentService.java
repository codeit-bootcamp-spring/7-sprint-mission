package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto);
    BinaryContentResponseDto findById(UUID id);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> id);
    boolean delete(UUID id);
}
