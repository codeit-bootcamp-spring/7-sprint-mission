package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    public BinaryContentResponse create(BinaryContentCreateRequest dto);
    public BinaryContentResponse get(UUID uuid);
    public List<BinaryContentResponse> getAllByUserID(String userId);

    public void delete(UUID uuid);
}
