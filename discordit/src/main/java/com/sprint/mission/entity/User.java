package com.sprint.mission.entity;

import java.util.Objects;

public class User extends BaseEntity implements Receivable {

    private final String userId;
    private String passwd;

    private String displayName;
    private String bio;
    private Status onlineStatus;

    public User(String userId, String passwd, String displayName) {
        this.userId = userId;
        this.passwd = passwd;
        this.displayName = displayName;
        this.onlineStatus = Status.OFFLINE;
    }

    public String getUserId() {
        return userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.updatedAt = getUnixTimestamp();
        this.passwd = passwd;
    }

    public boolean login(String id, String passwd){
        return this.userId.equals(id) && this.passwd.equals(passwd);
    }

    @Override
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

    public static boolean validateId(String id){
        if (id.length() < 4 || id.length() > 10){
            throw new IllegalArgumentException("아이디는 4에서 10자 사이로 입력해주세요.");
        }
        return true;
    }

    public static boolean validatePasswd(String passwd) {
        // 지금은 아이디와 제약사항이 같지만 나중에 변경될 수 있어 분리함
        if (passwd.length() < 4 || passwd.length() > 10){
            throw new IllegalArgumentException("비밀번호는 4에서 10자 사이로 입력해주세요.");
        }
        return true;
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

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", Passwd='" + passwd + '\'' +
                ", displayName='" + displayName + '\'' +
                ", bio='" + bio + '\'' +
                ", onlineStatus=" + onlineStatus +
                '}';
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
