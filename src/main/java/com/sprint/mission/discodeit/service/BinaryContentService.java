package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent createBinaryContent(CreateBinaryContentDto createDtoBinaryContentDto);

    BinaryContent getBinaryContent(UUID binaryContentId);

    List<BinaryContent> getAllBinaryContentByIdIn(List<UUID> binaryContentIds);

    void deleteBinaryContent(UUID binaryContentId);

}
