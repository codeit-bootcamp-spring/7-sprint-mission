package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface InterfaceBinaryContentService {
    BinaryContentDto find(UUID binaryContentId);
    List<BinaryContentDto> findAllByIdIn(UUID[] binaryContentIds);
    BinaryContentDto updateStatus(UUID binaryContentId, BinaryContentStatus sataus);
}
