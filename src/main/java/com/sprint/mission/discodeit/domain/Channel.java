package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel {

    private String id;
    private Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType type;
    private final List<String> members;

    public Channel(String name, String description, boolean isPrivate, List<String> members) {
        validateChannelName(name);

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        if (isPrivate) {
            this.type = ChannelType.PRIVATE;
        } else {
            this.type = ChannelType.PUBLIC;
        }
        if (members == null) {
            this.members = new ArrayList<>();
        } else {
            this.members = members;
        }
    }

    public void updateChannelName(String name) {
        validateChannelName(name);
        this.name = name;
    }

    private void validateChannelName(String name) {
        if (name == null || name.length() < 1) {
            throw new IllegalArgumentException("채널 이름을 입력하세요");
        }
    }

    public void addMember(String userId) {
        if (members
                .stream()
                .anyMatch(uuid -> uuid.equals(userId))) {
            throw new IllegalArgumentException("해당 유저는 이미 채널에 있습니다.");
        }
        members.add(userId);
    }
}