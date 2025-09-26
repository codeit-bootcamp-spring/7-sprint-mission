package com.sprint.mission.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private User sender;
    private User receiver;

    public Message(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
