package com.sprint.mission.discodeit.entity;

import java.util.UUID;



public class FriendRequest {

    private final UUID id;
    private User sender;
    private User receiver;


    public FriendRequest() {
        this.id = UUID.randomUUID();
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }


}
