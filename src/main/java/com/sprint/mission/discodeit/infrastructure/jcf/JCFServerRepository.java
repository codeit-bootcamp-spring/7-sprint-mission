package com.sprint.mission.discodeit.infrastructure.jcf;

import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import com.sprint.mission.discodeit.domain.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf",
matchIfMissing = true)
public class JCFServerRepository implements ServerRepository {

    private final Map<UUID, Server> store = new HashMap<>();

    @Override
    public void save(Server server){
        UUID key = server.getId();
        store.put(key, server);
    }
    @Override
    public void remove(Server server){
        UUID findChannelId = server.getId();
        store.remove(findChannelId);
    }
    @Override
    public Optional<Server> findById(UUID id){
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Server> findAll() {
        return List.copyOf(store.values().stream().toList());
    }

}
