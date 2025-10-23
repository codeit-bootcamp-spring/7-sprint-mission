package com.sprint.mission.discodeit.server.presentation;

import com.sprint.mission.discodeit.server.domain.Server;

import java.util.List;
import java.util.UUID;

public interface ServerService {

    void save(Server Server);

    void remove(Server Server);

    Server findById(UUID id);

    List<Server> findAll();

}
