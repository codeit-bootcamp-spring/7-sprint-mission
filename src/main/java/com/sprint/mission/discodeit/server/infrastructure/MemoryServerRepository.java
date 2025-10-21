package com.sprint.mission.discodeit.server.infrastructure;

import com.sprint.mission.discodeit.server.application.ServerRepository;
import com.sprint.mission.discodeit.server.domain.Server;

import java.util.*;

public class MemoryServerRepository implements ServerRepository {

    private final Map<UUID, Server> store = new HashMap<>();

    public void save(Server server){
        UUID key = server.getId();
        store.put(key, server);
    }
    public void remove(Server server){
        UUID findChannelId = server.getId();
        store.remove(findChannelId);
    }

    public Optional<Server> findById(UUID id){
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Server> findAll() {
        return List.copyOf(store.values().stream().toList());
    }

}
