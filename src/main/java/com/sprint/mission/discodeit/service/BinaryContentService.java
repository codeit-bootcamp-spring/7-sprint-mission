package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateRequest request);

    BinaryContentDto find(UUID BinaryContentId);

    List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

    void delete(UUID BinaryContentId);

    ResponseEntity<?> download(UUID binaryContentId);
}
