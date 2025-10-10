package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FriendShip {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private final UUID userAId;
    private final UUID userBId;


    public FriendShip(UUID userAId, UUID userBId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.userAId=userAId;
        this.userBId=userBId;
    }
}
