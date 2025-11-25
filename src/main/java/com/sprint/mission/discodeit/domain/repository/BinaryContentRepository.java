package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);

    void delete(BinaryContent binaryContent);

    Optional<BinaryContent> findById(String id);

    List<BinaryContent> findAll();
}
