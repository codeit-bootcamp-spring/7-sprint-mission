package com.sprint.mission.discodeit.infrastructure.jcf;

import com.sprint.mission.discodeit.domain.friendship.FriendShipRepository;
import com.sprint.mission.discodeit.domain.friendship.FriendShip;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf")
public class JCFFriendShipRepository implements FriendShipRepository {

    private final Map<UUID, FriendShip> store = new HashMap<>();


    @Override
    public void save(FriendShip friendShip) {
        UUID key = friendShip.getId();
        store.put(key, friendShip);
    }

    @Override
    public void remove(FriendShip friendShip) {
        UUID key = friendShip.getId();
        store.remove(key, friendShip);
    }

    @Override
    public Optional<FriendShip> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<FriendShip> findAll() {
        return List.copyOf(store.values().stream().toList());
    }
}
