package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentGetRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponse create(BinaryContentCreateRequest dto);

    BinaryContentResponse get(UUID id);
    List<BinaryContentResponse> getAllById(BinaryContentGetRequest dto);
    List<BinaryContentResponse> getAllByUserId(String userId);

    void delete(UUID uuid);
}
