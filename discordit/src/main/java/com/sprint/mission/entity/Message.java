package com.sprint.mission.entity;

import java.util.UUID;

public class Message {
    private final UUID uuid = UUID.randomUUID();
    private Long createdAt;
    private Long updatedAt;

    private User sender;
    private User receiver;

}
