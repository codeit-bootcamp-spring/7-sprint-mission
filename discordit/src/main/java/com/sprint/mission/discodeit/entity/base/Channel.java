package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;

import java.time.Instant;
import java.util.*;

@Getter
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


    public void setChannelName(String channelName) {
        this.channelName = channelName;
        update();
    }

    public void setType(ChannelType type) {
        this.type = type;
        update();
    }

    public void setMembers(Set<User> members) {
        this.members = members;
        update();
    }

    public void addMember(User user){
        this.members.add(user);
        update();
    }

    public void deleteMember(User user) {
        this.members.remove(user);
        update();
    }

    public void setModerators(Set<User> moderators) {
        this.moderators = moderators;
        update();
    }

    public void addModerator(User user){
        this.members.add(user);
        this.moderators.add(user);
        update();
    }

    public void deleteModerator(User user) {
        this.moderators.remove(user);
        update();
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

    public static Channel fromDto(UUID uuid, Instant createdAt, Instant updatedAt,
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
