package com.sprint.mission.discodeit.content.binary;

import com.sprint.mission.discodeit.common.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository extends BaseRepository<BinaryContent, UUID> {
    List<BinaryContent> findAllByOwnerId(UUID ownerId);
    List<BinaryContent> findAllByFilePath(String filePath);
    void deleteAllByOwnerId(UUID ownerId);
}
