package com.sprint.mission.discodeit.service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseRepository<T> {

    void save(T t);

    void remove(T t);

    Optional<T> findById(UUID id);

    List<T> findAll();
}
