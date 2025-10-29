package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Channel extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private String name;            //채널명
    private final UUID managerId;   //채널 생성자 UUID
    private List<UUID> users;       //채널 참가자

    //Constructor
    public Channel(UUID managerId, String name) {
        this.name = name;
        this.managerId = managerId;
    }

    //update name
    public Channel update(String name){
        super.update();
        this.name = name;
        return this;
    }
}
