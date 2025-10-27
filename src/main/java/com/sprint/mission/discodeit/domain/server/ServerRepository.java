package com.sprint.mission.discodeit.domain.server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerRepository {
    void save(Server server);

    void remove(Server server);

    Optional<Server> findById(UUID id);

    List<Server> findAll();
}
