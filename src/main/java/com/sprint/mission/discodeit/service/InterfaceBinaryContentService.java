package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface InterfaceBinaryContentService {
//    BinaryContentDto create(Dto_BinaryContent dtoBinaryContent);
    BinaryContentDto find(UUID binaryContentId);
    List<BinaryContentDto> findAllByIdIn(UUID[] binaryContentIds);
//    public void delete(UUID binaryContentId);
    ResponseEntity<Resource> download(UUID binaryContentId);
}
