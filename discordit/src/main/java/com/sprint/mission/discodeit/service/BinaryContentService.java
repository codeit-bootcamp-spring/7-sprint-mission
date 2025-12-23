package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.entity.binaryContent.request.BinaryContentCreateRequest;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDto create(BinaryContentCreateRequest dto);

    BinaryContentDto get(UUID id);

    List<BinaryContentDto> getByIds(List<UUID> binaryContentIds);
}
