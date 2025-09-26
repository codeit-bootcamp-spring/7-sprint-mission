package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//서버
public class Channel {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;


    private User manager;
    private String serverName;
    private int serverLevel;
    private List<Message> chatRooms=new ArrayList<>();
    private String topic;
    private boolean isPrivate;
    private State state;
    //채널타입




    public Channel() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }


    public void update(){
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }



    public List<Message> getChatRooms() {
        return chatRooms;
    }
}
