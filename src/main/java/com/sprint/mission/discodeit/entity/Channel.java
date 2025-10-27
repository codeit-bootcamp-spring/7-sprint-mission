package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Channel extends BaseEntity{
    private String channelName;
    private final ChannelType channelType;
    private User admin;
    private List<User> members;

    public Channel(ChannelType channelType, String channelName, User admin) {
        this.channelType = channelType;
        this.channelName = channelName;
        this.admin = admin;
        this.members = new ArrayList<>();
        this.members.add(admin);
    }

    public void setChannelName(String channelName) {
        this.setUpdatedAt();
        this.channelName = channelName;
    }

    public void setAdmin(User admin) {
        this.setUpdatedAt();
        this.admin = admin;
    }

    public List<User> getMembers() {
        return new ArrayList<>(members);
    }

    public void addMember(User user) {
        this.setUpdatedAt();
        this.members.add(user);
    }

    public void delMember(User user){
        this.setUpdatedAt();
        this.members.remove(user);
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelType=" + channelType +
                ", admin=" + admin.getUserName() +
                ", members=" + members.stream().map(m -> m.getUserName()).toList() +
                str +
                '}';
    }
}
