package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentUploadRequest;

import java.util.UUID;

public interface BinaryContentService {

    BinaryContentDto upload(BinaryContentUploadRequest req);

    BinaryContentDto find(UUID id);

    void delete(UUID id);
}
