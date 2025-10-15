package com.sprint.mission.discodeit.entity;



import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//서버
@Getter
@Setter
public class Channel implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String serverName;
    private Long serverLevel;
    private boolean isPrivate;
    private final List<UUID> members = new ArrayList<>();
    private final List<UUID> messageRooms = new ArrayList<>();


    public Channel() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public void updateServerName(String newServerName){
        this.updatedAt=System.currentTimeMillis();
        this.serverName=serverName;
    }


    public void updateServeLevel(Long newServerLevel){
        this.updatedAt=System.currentTimeMillis();
        this.serverLevel=newServerLevel;
    }

    public void updatePrivate(boolean b){
        this.updatedAt=System.currentTimeMillis();
        this.isPrivate=b;
    }

    public List<UUID> getMembers() {
        return List.copyOf(members);
    }

    public void addMember(UUID id){
        members.add(id);
        updatedAt=System.currentTimeMillis();
    }
    public void removeMember(UUID id){
        members.remove(id);
        updatedAt=System.currentTimeMillis();
    }

    public List<UUID> getMessageRooms() {
        return List.copyOf(messageRooms);
    }

    public void addMessageRoom(UUID id){
        messageRooms.add(id);
        updatedAt=System.currentTimeMillis();
    }

}
