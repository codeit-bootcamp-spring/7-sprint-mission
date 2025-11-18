package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto);
    Optional<BinaryContentResponseDto> findById(UUID id);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> id);
    boolean delete(UUID id);
}
