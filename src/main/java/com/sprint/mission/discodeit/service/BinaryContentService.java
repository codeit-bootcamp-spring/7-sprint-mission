package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResponseDto createBinaryContent(CreateBinaryContentRequestDto CreateDto);
    BinaryContentResponseDto find(UUID BinaryContentId);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> BinaryContentIds);
    void delete(UUID BinaryContentId);
}
