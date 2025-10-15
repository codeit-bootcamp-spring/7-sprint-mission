package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseRepository<T> {

    T save(T t);

    Optional<T> findById(UUID id);

    List<T> findAll();

    // 물리 삭제
    void deleteById(UUID id);

}
