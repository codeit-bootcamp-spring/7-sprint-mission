package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    void createBinaryContent(CreateBinaryContentRequestDto request);
    BinaryContent findBinaryContent(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> idList);
    void deleteBinaryContent(UUID id);
}
