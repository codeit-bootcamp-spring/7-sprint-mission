package com.sprint.mission.entity;

public class User extends BaseEntity {

    private String userId;
    private String Passwd;

    private String displayName;
    private String bio;
    private Status onlineStatus;

    public String getUserId() {
        return userId;
    }

    public void updateUserId(String userId) {
        this.userId = userId;
        this.updatedAt = getUnixTimestamp();
    }

    public String getPasswd() {
        return Passwd;
    }

    public void updatePasswd(String passwd) {
        this.updatedAt = getUnixTimestamp();
        Passwd = passwd;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void updateDisplayName(String displayName) {
        this.updatedAt = getUnixTimestamp();
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void updateBio(String bio) {
        this.updatedAt = getUnixTimestamp();
        this.bio = bio;
    }

    public Status getOnlineStatus() {
        return onlineStatus;
    }

    public void updateOnlineStatus(Status onlineStatus) {
        this.updatedAt = getUnixTimestamp();
        this.onlineStatus = onlineStatus;
    }

    private enum Status{
        ONLINE("온라인"),
        OFFLINE("오프라인"),
        AWAY("자리비움"),
        DO_NOT_DISTURB("방해금지");

        Status(String description){
        }

    }


}
