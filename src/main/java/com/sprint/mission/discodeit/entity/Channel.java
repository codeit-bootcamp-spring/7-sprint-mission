package com.sprint.mission.discodeit.entity;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//서버

public class Channel {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private User manager;
    private String serverName;
    private Long serverLevel;
    private boolean isPrivate;
    private List<User> Members = new ArrayList<>();


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

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }



    public String getServerName() {
        return serverName;
    }

    public Long getServerLevel() {
        return serverLevel;
    }

    public List<User> getMembers() {
        return Members;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setServerLevel(Long serverLevel) {
        this.serverLevel = serverLevel;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", serverName='" + serverName + '\'' +
                ", serverLevel=" + serverLevel + '\'' +
                ", isPrivate=" + isPrivate +
                '}';
    }

}
