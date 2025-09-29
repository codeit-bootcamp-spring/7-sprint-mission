package com.sprint.mission.discodeit.entity;

public class UpdatedChannelDTO {
    private User manager;
    private String serverName;
    private Long serverLevel;
    private boolean isPrivate;

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Long getServerLevel() {
        return serverLevel;
    }

    public void setServerLevel(Long serverLevel) {
        this.serverLevel = serverLevel;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
