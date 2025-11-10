package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.Server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerRepository {
    void save(Server server);

    void remove(UUID id);

    Optional<Server> findById(UUID id);

    List<Server> findAll();
}
