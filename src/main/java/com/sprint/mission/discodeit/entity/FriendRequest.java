package com.sprint.mission.discodeit.entity;

import java.util.UUID;



public class FriendRequest {

    private final UUID id;
    private UUID senderId;
    private UUID receiverId;


    public FriendRequest() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }
}
