package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

@Getter
public class Channel extends BaseEntity implements Receivable {

    private String channelName;
    private final ChannelScope scope;
    private final ChannelType type;
    private String description;

    private Set<User> members; // 채널 등록 멤버 목록
    private Set<User> moderators;// 운영진

    public Channel(String channelName, ChannelScope scope, ChannelType type, Set<User> moderators) {
        this.channelName = channelName;
        this.scope = scope;
        this.type = type;
        this.members = new HashSet<>();
        this.moderators = moderators;
    }


    public void setChannelName(String channelName) {
        this.channelName = channelName;
        update();
    }

    public void setMembers(Set<User> members) {
        this.members = members;
        update();
    }

    public void setDescription(String description) {
        this.description = description;
        update();
    }

    public void addMember(User user){
        this.members.add(user);
        update();
    }
    public void addMembers(Set<User> user){
        this.members.addAll(user);
        update();
    }

    public void deleteMember(User user) {
        this.members.remove(user);
        update();
    }

    public void deleteMember(Set<User> users) {
        this.members.removeAll(users);
        update();
    }

    public void addModerators(Set<User> moderators) {
        this.moderators.addAll(moderators);
        this.members.addAll(moderators);
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

    public void deleteModerator(Set<User> users) {
        this.moderators.removeAll(users);
        update();
    }

    public String getDisplayName() {
        return channelName;
    }

    public static Channel fromDto(UUID uuid, Instant createdAt, Instant updatedAt,
                                  String channelName, String description, ChannelScope scope, ChannelType type,
                                  Set<User> moderators, Set<User> members) {
        Channel channel = new Channel(channelName, scope, type, moderators);
        channel.createdAt = createdAt;
        channel.uuid = uuid;
        channel.description = description;
        channel.members = members;
        channel.updatedAt = updatedAt;
        return channel;
    }

}
