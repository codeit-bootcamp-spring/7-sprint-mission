package com.sprint.mission.discodeit.application.repository;

import com.sprint.mission.discodeit.domain.FriendRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRequestRepository {
    void save(FriendRequest friendRequest);

    void remove(FriendRequest friendRequest);

    Optional<FriendRequest> findById(UUID id);

    List<FriendRequest> findAll();


}
