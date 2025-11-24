package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.dto.Res_BinaryContent;

import java.util.List;
import java.util.UUID;

public interface InterfaceBinaryContentService {
    Res_BinaryContent create(Dto_BinaryContent dtoBinaryContent);
    Res_BinaryContent find(UUID binaryContentId);
    List<Res_BinaryContent> findAllByIdIn(UUID[] binaryContentIds);
    public void delete(UUID binaryContentId);
}
