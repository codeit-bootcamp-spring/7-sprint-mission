package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.dto.dto_Neo.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface InterfaceBinaryContentService {
    BinaryContentDto find(UUID binaryContentId);
    List<BinaryContentDto> findAllByIdIn(UUID[] binaryContentIds);
    BinaryContentDto updateStatus(UUID binaryContentId, BinaryContentStatus sataus);
}
