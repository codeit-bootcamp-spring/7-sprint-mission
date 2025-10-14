package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends BasicEntity{

    private String channelName; //채널 이름
    private final List<UUID> members; // 채널 멤버 목록


    public Channel(String channelName) {
        super();
        this.channelName = channelName;
        this.members = new ArrayList<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public List<UUID> getMembers() {
        return new ArrayList<>(members);
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
        update();
    }

    public void addMember(UUID userId){
        if(!members.contains(userId)){
            members.add(userId);
            update();
        }
    }

    public void removeMember(UUID userId){
        members.remove(userId);
        update();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", members=" + members.size() + "명" +
                '}';
    }
}
