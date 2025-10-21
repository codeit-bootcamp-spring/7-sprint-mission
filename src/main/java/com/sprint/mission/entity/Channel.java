package com.sprint.mission.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Channel extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    // 필드(변수)
     private String channelName;
     private User bose;
     private List<User> users;
    //카테고리다 channel
 //   private List<Category> category;
 //   private String imageUrl;
 //   private Message channelMessage;


    public Channel(User user,String channelName) {
        this.channelName = channelName;
        this.users = new ArrayList<>();
        this.bose = user;
        this.users.add(user);
    }

    public User getBose() {
        return bose;
    }

    public void setBose(User bose) {
        this.bose = bose;

    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "주인=" + bose.getUserName() +
                ", 유저들=" + users.stream()
                .map(User::getUserName)
                .toList()
                +
                '}';
    }
}
