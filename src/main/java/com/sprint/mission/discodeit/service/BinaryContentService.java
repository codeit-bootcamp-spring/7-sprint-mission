package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.enums.BinaryContentStatus;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponseDto createBinaryContent(CreateBinaryContentDto createDtoBinaryContentDto);

    BinaryContentResponseDto getBinaryContent(UUID binaryContentId);

    List<BinaryContentResponseDto> getAllBinaryContentByIdIn(List<UUID> binaryContentIds);

    void deleteBinaryContent(UUID binaryContentId);

    void updateBinaryContentStatus(UUID binaryContentId, BinaryContentStatus status);

}
