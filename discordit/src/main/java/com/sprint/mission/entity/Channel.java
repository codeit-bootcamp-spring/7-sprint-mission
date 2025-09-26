package com.sprint.mission.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends BaseEntity {

    private ChannelType type;

    private List<User> members; // 채널 등록 멤버 목록
    private List<User> onlineUsers; // 접속 중 멤버
    private List<User> moderators;// 운영진

    public Channel(ChannelType type, List<User> moderators) {
        this.type = type;
        this.members = new ArrayList<>();
        this.onlineUsers = new ArrayList<>();
        this.moderators = moderators;
    }

    public void updateType(ChannelType type) {
        this.type = type;
        updatedAt = getUnixTimestamp();
    }

    public void updateMembers(List<User> members) {
        this.members = members;
        updatedAt = getUnixTimestamp();

    }

    public void updateOnlineUsers(List<User> onlineUsers) {
        this.onlineUsers = onlineUsers;
        updatedAt = getUnixTimestamp();
    }

    public void updateModerators(List<User> moderators) {
        this.moderators = moderators;
        updatedAt = getUnixTimestamp();
    }

    public ChannelType getType() {
        return type;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }

    public List<User> getModerators() {
        return moderators;
    }

    public enum ChannelType {
        TEXT("텍스트"),
        VOICE("음성");

        ChannelType(String description) {
        }
    }

}
