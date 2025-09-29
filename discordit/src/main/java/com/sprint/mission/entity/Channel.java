package com.sprint.mission.entity;

import java.util.*;

public class Channel extends BaseEntity {

    private String channelName;
    private ChannelType type;

    private Set<User> members; // 채널 등록 멤버 목록
    private Set<User> moderators;// 운영진

    public Channel(String channelName, ChannelType type, Set<User> moderators) {
        this.channelName = channelName;
        this.type = type;
        this.members = new HashSet<>();
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

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public void addMember(User user){
        this.members.add(user);
    }

    public void deleteMember(User user) {
        this.members.remove(user);
    }

    public Set<User> getModerators() {
        return moderators;
    }

    public void setModerators(Set<User> moderators) {
        this.moderators = moderators;
    }

    public void addModerator(User user){
        this.members.add(user);
        this.moderators.add(user);
    }

    public void deleteModerator(User user) {
        this.moderators.remove(user);
    }

    public enum ChannelType {
        TEXT("텍스트"),
        VOICE("음성");

        ChannelType(String description) {
        }
    }

}
