package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User extends BaseEntity{
    private String userName;
    private String nickName;
    private String email;
    private String phoneNum;
    private final String userId;
    private String password;
    private List<UUID> channelIds;

    public User(String userName, String nickName, String email, String phoneNum, String userId, String password) {
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.userId = userId;
        this.password = password;
        this.channelIds = new ArrayList<>();
    }

    //setter 처리 추가 필요
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.setUpdatedAt();
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.setUpdatedAt();
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.setUpdatedAt();
        this.phoneNum = phoneNum;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.setUpdatedAt();
        this.password = password;
    }

    public List<UUID> getChannelIds() {
        return channelIds;
    }

    public void addChannelId(UUID id) {
        this.channelIds.add(id);
    }

    public void delChannelId(UUID id) {
        this.channelIds.remove(id);
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "User{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                str +
                ", channelIds='" + channelIds + '\'' +
                '}';
    }
}
