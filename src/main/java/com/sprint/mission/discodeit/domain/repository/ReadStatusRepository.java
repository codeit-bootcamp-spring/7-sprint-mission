package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

    void save(ReadStatus readStatus);

    void remove(UUID id);

    Optional<ReadStatus> findById(UUID id);

    List<ReadStatus> findAll();
}
