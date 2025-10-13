package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User extends CommonModel {
    private String userName;
    private UUID ownChannelID; // 방장인 경우

    public User(String name) {
        super();
        this.userName = name;
        this.ownChannelID = null;
    }

    //===========================
    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public UUID getOwnChannelID() {
        return this.ownChannelID;
    }

    public void setOwnChannelID(UUID ownChannelID) {
        this.ownChannelID = ownChannelID;
    }

    @Override
    public String toString() {
        return "user {" +
                super.toString() +
                "\n name = [" + userName + "] }";
    }
}
