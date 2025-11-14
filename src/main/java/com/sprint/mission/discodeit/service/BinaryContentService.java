package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContent binaryContent);
    BinaryContent findById(UUID id);
    List<BinaryContent> findAll();
    void delete(UUID id);

    BinaryContentInfoRes getBinaryContent(UUID id);
    List<BinaryContentInfoRes> getBinaryContentList();
}
