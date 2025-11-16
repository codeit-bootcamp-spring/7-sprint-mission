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
    private ChannelType type;
    private String description;

    private final Set<User> members = new HashSet<>(); // 채널 등록 멤버 목록
    private final Set<User> moderators = new HashSet<>();// 운영진

    // 기존 코드와의 호환성을 위해 남김
    private Channel(ChannelScope scope) {
        this.scope = scope;
    }

    public static Channel newPublicChannel(String channelName, String description) {
        Channel channel = new Channel(ChannelScope.PUBLIC);
        channel.channelName = channelName;
        channel.description = description;
        return channel;
    }

    public static Channel newPrivateChannel(Set<User> members) {
        Channel channel = new Channel(ChannelScope.PRIVATE);
        channel.channelName = null;
        channel.members.addAll(members);
        return channel;
    }


    public void setChannelName(String channelName) {
        this.channelName = channelName;
        update();
    }

    public void setMembers(Set<User> members) {
        this.members.clear();
        this.members.addAll(members);
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

    // TODO: 시그니처 수정
    public static Channel fromDto(UUID uuid, Instant createdAt, Instant updatedAt,
                                  String channelName, String description, ChannelScope scope, ChannelType type,
                                  Set<User> moderators, Set<User> members) {
        Channel channel = new Channel(scope);
        channel.createdAt = createdAt;
        channel.uuid = uuid;
        channel.description = description;
        channel.setMembers(members);
        channel.updatedAt = updatedAt;
        channel.channelName = channelName;
        return channel;
    }

}
