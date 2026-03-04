package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    void create(CreateBinaryContentRequestDto request);
    BinaryContentResponseDto find(UUID binaryContentId);
    BinaryContent download(UUID binaryContentId);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);
    BinaryContentResponseDto updateStatus(UUID binaryContentId, BinaryContentStatus newStatus);
}
