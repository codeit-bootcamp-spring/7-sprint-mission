package com.sprint.mission.discodeit.application.repository;

import com.sprint.mission.discodeit.domain.FriendShip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendShipRepository {
    void save(FriendShip friendShip);

    void remove(FriendShip friendShip);

    Optional<FriendShip> findById(UUID id);

    List<FriendShip> findAll();
}
