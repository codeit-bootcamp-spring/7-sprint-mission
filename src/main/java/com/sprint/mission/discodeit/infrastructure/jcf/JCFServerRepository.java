package com.sprint.mission.discodeit.infrastructure.jcf;

import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import com.sprint.mission.discodeit.domain.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
@Slf4j
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf",
matchIfMissing = true)
public class JCFServerRepository implements ServerRepository {

    private final Map<UUID, Server> store = new HashMap<>();

    public JCFServerRepository() {
        UUID uuid = UUID.fromString("12121212-1212-1212-1212-121212343434");
        List<UUID> members = new ArrayList<>();
        UUID uuid1 = UUID.fromString("32121212-1212-1212-1212-121212343434");
        members.add(uuid1);
        Server server = new Server("test1", false, 1L,members);
        store.put(uuid, server);

        UUID uuid2 = UUID.fromString("22121212-1212-1212-1212-121212343434");
        Server server2 = new Server("test2", false, 1L,members);
        store.put(uuid2, server2);
    }

    @Override
    public void save(Server server){
        UUID key = server.getId();
        store.put(key, server);
    }
    @Override
    public void remove(UUID id){
        store.remove(id);
        log.info("삭제 완료");

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
