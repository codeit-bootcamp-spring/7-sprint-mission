package com.sprint.mission.entity;

import java.io.Serializable;
import java.util.*;

public class Channel extends BaseEntity implements Receivable {

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
        this.updatedAt = getUnixTimestamp();
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
        this.updatedAt = getUnixTimestamp();
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
        this.updatedAt = getUnixTimestamp();
    }

    public void addMember(User user){
        this.members.add(user);
        this.updatedAt = getUnixTimestamp();
    }

    public void deleteMember(User user) {
        this.members.remove(user);
        this.updatedAt = getUnixTimestamp();
    }

    public Set<User> getModerators() {
        return moderators;
    }

    public void setModerators(Set<User> moderators) {
        this.moderators = moderators;
        this.updatedAt = getUnixTimestamp();
    }

    public void addModerator(User user){
        this.members.add(user);
        this.moderators.add(user);
        this.updatedAt = getUnixTimestamp();
    }

    public void deleteModerator(User user) {
        this.moderators.remove(user);
        this.updatedAt = getUnixTimestamp();
    }

    public String getDisplayName() {
        return channelName;
    }

    public enum ChannelType {
        TEXT("텍스트"),
        VOICE("음성");

        ChannelType(String description) {
        }
    }

    public static Channel rehydrate(UUID uuid, Long createdAt, Long updatedAt,
                                    String channelName, ChannelType type,
                                    Set<User> moderators, Set<User> members) {
        Channel channel = new Channel(channelName, type, moderators);
        channel.uuid = uuid;
        channel.createdAt = createdAt;
        channel.updatedAt = updatedAt;
        channel.members = members;
        return channel;
    }

}
