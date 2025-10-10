package com.sprint.mission.discodeit.entity;

import lombok.Getter;


import java.util.UUID;


@Getter
public class FriendRequest {

    private final UUID id;
    private final UUID senderId;
    private final UUID receiverId;
    private final Long createdAt;
    



    public FriendRequest(UUID senderId, UUID receiverId) {
        this.id= UUID.randomUUID();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createdAt=System.currentTimeMillis();
    }


}
