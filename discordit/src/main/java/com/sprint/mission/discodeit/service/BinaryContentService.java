package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentGetRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponse create(BinaryContentCreateRequest dto);
    List<BinaryContentResponse> get(BinaryContentGetRequest dto);
    List<BinaryContentResponse> getAllByUserID(String userId);

    void delete(UUID uuid);
}
