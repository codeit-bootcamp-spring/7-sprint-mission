package com.sprint.mission.discodeit.friendship.infrastructure;

import com.sprint.mission.discodeit.friendship.domain.FriendShip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendShipRepository {
    void save(FriendShip friendShip);

    void remove(FriendShip friendShip);

    Optional<FriendShip> findById(UUID id);

    List<FriendShip> findAll();
}
