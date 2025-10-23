package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class Channel extends BaseEntity{
    //Field
    private String name;            //채널명
    private final UUID managerId;   //채널 생성자 UUID
    private List<UUID> users;       //채널 참가자

    //Constructor
    public Channel(String name, UUID managerId) {
        this.name = name;
        this.managerId = managerId;
    }
}
