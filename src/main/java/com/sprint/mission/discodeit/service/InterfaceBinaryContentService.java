package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface InterfaceBinaryContentService {
//    BinaryContentDto create(Dto_BinaryContent dtoBinaryContent);
    BinaryContentDto find(UUID binaryContentId);
    List<BinaryContentDto> findAllByIdIn(UUID[] binaryContentIds);
//    public void delete(UUID binaryContentId);
    void download(UUID binaryContentId);
}
