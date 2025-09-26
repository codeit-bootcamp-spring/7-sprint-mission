package com.sprint.mission.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private User sender;
    private User receiver;
    private String message;

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }
}
