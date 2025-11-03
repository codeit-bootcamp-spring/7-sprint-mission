package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.*;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binary);

    Optional<BinaryContent> findById(UUID id);

    Optional<BinaryContent> findByUserId(UUID userId);

    List<BinaryContent> findAllByMessageId(UUID messageId);

    List<BinaryContent> findAllByIdIn(List<UUID> ids);

    void deleteById(UUID id);

    void deleteByUserId(UUID userId);

    void deleteAllByMessageId(UUID messageId);

    default boolean existsById(UUID id) {
        return findById(id).isPresent();
    }
}
