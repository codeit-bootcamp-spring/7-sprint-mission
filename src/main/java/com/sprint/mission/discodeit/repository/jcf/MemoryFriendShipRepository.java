package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.FriendShip;
import com.sprint.mission.discodeit.repository.FriendShipRepository;

import java.util.*;

public class MemoryFriendShipRepository implements FriendShipRepository {

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
