package com.sprint.mission.discodeit.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String name;
    private User manager;
    private List<User> users = new ArrayList<>();

    //기본 생성자
    public Channel(){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    //이름
    public Channel(String name, User manager){
        this();
        this.name = name;
        this.manager = manager;
    }
}
