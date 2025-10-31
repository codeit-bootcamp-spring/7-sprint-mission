package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContent binaryContent);
    BinaryContent findById(UUID id);
    List<BinaryContent> findAll();
    BinaryContent delete(UUID id);
}
