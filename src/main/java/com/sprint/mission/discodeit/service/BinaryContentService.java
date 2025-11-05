package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
   ResponseEntity<BinaryContentResponse> create(BinaryContentCreateRequest request );
    BinaryContentResponse find(UUID BinaryContentId);
    List<BinaryContentResponse> findAllByIn(UUID BinaryContentId);
    void delete(UUID BinaryContentId);

}
