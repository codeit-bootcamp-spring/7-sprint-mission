package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    List<BinaryContent> findAll();
    BinaryContent findById(UUID id);
    BinaryContent delete(UUID id);
}
