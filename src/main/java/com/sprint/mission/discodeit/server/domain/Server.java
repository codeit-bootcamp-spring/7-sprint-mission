package com.sprint.mission.discodeit.server.domain;



import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//서버
@Getter
@Setter
public class Server implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String serverName;
    private Long serverLevel;
    private boolean isPrivate;
    private final List<UUID> members = new ArrayList<>();
    private final List<UUID> messageRooms = new ArrayList<>();

    //객체 생성을 위한 팩토리 메서드
    public static Server create(String serverName){
        validateServerName(serverName);
        return new Server(serverName);
    }

    private Server(String serverName) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.serverName=serverName;
        this.serverLevel=1L;
        this.isPrivate=false;
    }

    private static void validateServerName(String serverName){
        if (serverName == null || serverName.length()<1){
            throw new IllegalArgumentException("서버의 이름을 입력하세요");
        }
    }


    public void updateServerName(String newServerName){
        this.updatedAt=Instant.now();
        this.serverName=serverName;
    }


    public void updateServeLevel(Long newServerLevel){
        this.updatedAt=Instant.now();
        this.serverLevel=newServerLevel;
    }

    public void updatePrivate(boolean b){
        this.updatedAt=Instant.now();
        this.isPrivate=b;
    }

    public List<UUID> getMembers() {
        return List.copyOf(members);
    }

    public void addMember(UUID id){
        members.add(id);
        updatedAt=Instant.now();
    }
    public void removeMember(UUID id){
        members.remove(id);
        updatedAt=Instant.now();
    }

    public List<UUID> getMessageRooms() {
        return List.copyOf(messageRooms);
    }

    public void addMessageRoom(UUID id){
        messageRooms.add(id);
        updatedAt=Instant.now();
    }

}
