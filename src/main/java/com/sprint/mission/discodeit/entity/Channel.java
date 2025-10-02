package com.sprint.mission.discodeit.entity;



import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//서버
@Getter
@Setter
public class Channel {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String serverName;
    private Long serverLevel;
    private boolean isPrivate;
    private final List<UUID> Members = new ArrayList<>();


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
        return List.copyOf(Members);
    }

    /*
    * 멤버 추가 메서드
    *@param: User객체
    **/
    public void addMember(User user){
        Members.add(user.getId());
        updatedAt=System.currentTimeMillis();
    }
    public void removeMember(User user){
        Members.remove(user.getId());
        updatedAt=System.currentTimeMillis();
    }

}
