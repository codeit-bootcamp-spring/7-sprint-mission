package com.sprint.mission.discodeit.friendrequest.infrastructure;

import com.sprint.mission.discodeit.friendrequest.application.FriendRequestRepository;
import com.sprint.mission.discodeit.friendrequest.domain.FriendRequest;

import java.util.*;

public class JCFFriendRequestRepository implements FriendRequestRepository {

    private final Map<UUID, FriendRequest> store = new HashMap<>();


    @Override
    public void save(FriendRequest entity) {
        UUID key = entity.getId();
        store.put(key,entity);

    }

    @Override
    public void remove(FriendRequest entity) {
        UUID key = entity.getId();
        store.remove(key,entity);
    }

    @Override
    public Optional<FriendRequest> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<FriendRequest> findAll() {
        return List.copyOf(store.values().stream().toList());
    }
}
