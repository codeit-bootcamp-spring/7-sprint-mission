package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Res_BinaryContent;

import java.util.List;
import java.util.UUID;

public interface InterfaceBinaryContentService extends BaseInterfaceService {
    Res_BinaryContent create(Dto_BinaryContent dtoBinaryContent);
    Res_BinaryContent find(UUID binaryContentId);
    List<Res_BinaryContent> findAllByIdIn();
    public void delete(UUID binaryContentId);
}
