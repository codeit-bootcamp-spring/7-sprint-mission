package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class Channel {
    //Common field
    private UUID id;                //각 채널 UUID
    private Long createdAt;         //채널 생성일
    private Long updatedAt;         //채널 수정일

    //Message field
    private String name;            //채널명
    private final UUID managerId;   //채널 생성자 UUID
    private List<UUID> users;       //채널 참가자

    //Constructor
    public Channel(String name, UUID managerId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.managerId = managerId;
    }
}
