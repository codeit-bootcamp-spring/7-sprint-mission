package com.sprint.mission.discodeit.domain.friendship;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class FriendShip implements Serializable {
    private static final long serialVersionUID = 5L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final UUID userAId;
    private final UUID userBId;


    public FriendShip(UUID userAId, UUID userBId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userAId=userAId;
        this.userBId=userBId;
    }



}
