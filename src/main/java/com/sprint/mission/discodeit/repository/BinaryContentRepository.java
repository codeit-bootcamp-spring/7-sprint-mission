package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BinaryContentRepository {

    void save(BinaryContent binaryContent);

    BinaryContent findById(UUID id);

    void delete(UUID id);
}
