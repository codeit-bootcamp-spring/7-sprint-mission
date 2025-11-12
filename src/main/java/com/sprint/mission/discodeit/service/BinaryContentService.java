package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateRequest request );
    BinaryContent find(UUID BinaryContentId);
    List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID BinaryContentId);

}
