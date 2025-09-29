package com.sprint.mission.entity;

import java.util.Objects;

public class User extends BaseEntity {

    private String userId;
    private String Passwd;

    private String displayName;
    private String bio;
    private Status onlineStatus;

    public User(String userId, String passwd, String displayName) {
        this.userId = userId;
        Passwd = passwd;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getPasswd() {
        return Passwd;
    }

    public void setPasswd(String passwd) {
        this.updatedAt = getUnixTimestamp();
        Passwd = passwd;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.updatedAt = getUnixTimestamp();
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.updatedAt = getUnixTimestamp();
        this.bio = bio;
    }

    public Status getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Status onlineStatus) {
        this.updatedAt = getUnixTimestamp();
        this.onlineStatus = onlineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName);
    }

    public enum Status{
        ONLINE("온라인"),
        OFFLINE("오프라인"),
        AWAY("자리비움"),
        DO_NOT_DISTURB("방해금지");

        Status(String description){
        }

    }


}
