package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.ReadStatus;

import java.util.List;
import java.util.Optional;


public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

    void delete(ReadStatus readStatus);

    Optional<ReadStatus> findById(String id);

    List<ReadStatus> findAll();
}
