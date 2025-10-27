package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.content.BinaryContent;

import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent content);
    void delete(BinaryContent content);
    BinaryContent get(UUID uuid);
}
