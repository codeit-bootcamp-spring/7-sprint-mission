package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.Common;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    // 필드(변수)
     private String channelName;
     private UUID bose;
     private List<UUID> users;
    private ChannelType type;
    private String description;
    //카테고리다 channel
 //   private List<Category> category;
 //   private String imageUrl;
 //   private Message channelMessage;


    public Channel(UUID user, String channelName,ChannelType type,String description) {
        this.channelName = channelName;
        this.users = new ArrayList<>();
        this.bose = user;
        this.users.add(user);
        this.type = type;
        this.description = description;
    }

  /*  public UUID getBose() {
        return bose;
    }*/
/*
    public void setBose(UUID bose) {
        this.bose = bose;

    }

*//*    public List<UUID> getUsers() {
        return users;
    }*//*

    public void setUsers(List<UUID> users) {
        this.users = users;
    }

  *//*  public String getChannelName() {
        return channelName;
    }
*//*
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }*/

    @Override
    public String toString() {
        return "Channel{" +
                "주인= UUID" + bose +
                ", 유저들 UUID=" + users.stream()
                .toList()
                +
                '}';
    }

    public void update(String newChannelName, UUID newBose, List<UUID> newUsers) {
        boolean anyValueUpdated = false;

        if (newChannelName != null && !newChannelName.equals(this.channelName)) {
            this.channelName = newChannelName;
            anyValueUpdated = true;
        }

        if (newBose != null && !newBose.equals(this.bose)) {
            this.bose = newBose;
            anyValueUpdated = true;
        }

        if (newUsers != null && !newUsers.equals(this.users)) {
            this.users = newUsers;
            anyValueUpdated = true;
        }


        if (anyValueUpdated) {
            this.setUpdatedAt(Instant.ofEpochSecond(Instant.now().getEpochSecond()));
        }
    }
}
