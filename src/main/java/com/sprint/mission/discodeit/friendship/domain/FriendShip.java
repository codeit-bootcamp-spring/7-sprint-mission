package com.sprint.mission.discodeit.friendship.domain;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class FriendShip implements Serializable {
    private static final long serialVersionUID = 5L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private final UUID userAId;
    private final UUID userBId;


    public FriendShip(UUID userAId, UUID userBId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.userAId=userAId;
        this.userBId=userBId;

    }
}
