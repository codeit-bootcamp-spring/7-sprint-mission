package com.sprint.mission.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends BaseEntity {

    private String channelName;
    private ChannelType type;

    private List<User> members; // 채널 등록 멤버 목록
    private List<User> onlineUsers; // 접속 중 멤버
    private List<User> moderators;// 운영진

    public Channel(String channelName, ChannelType type, List<User> moderators) {
        this.channelName = channelName;
        this.type = type;
        this.members = new ArrayList<>();
        this.onlineUsers = new ArrayList<>();
        this.moderators = moderators;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(List<User> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public List<User> getModerators() {
        return moderators;
    }

    public void setModerators(List<User> moderators) {
        this.moderators = moderators;
    }

    public enum ChannelType {
        TEXT("텍스트"),
        VOICE("음성");

        ChannelType(String description) {
        }
    }

}
