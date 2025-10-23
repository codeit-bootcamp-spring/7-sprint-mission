package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.*;

import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binary);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    void deleteById(UUID id);
}
