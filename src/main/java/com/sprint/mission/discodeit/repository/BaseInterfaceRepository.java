package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseInterfaceRepository<T> {
    void save(T model);
    void deleteById(UUID id);
    Optional<T> findById(UUID id);
    Optional<List<T>> findAll();
    boolean existsById(UUID id);
    boolean existsByName(String name);
}
