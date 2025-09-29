package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends Common {
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
                "bose=" + bose +
                ", users=" + users +
                '}';
    }
}
