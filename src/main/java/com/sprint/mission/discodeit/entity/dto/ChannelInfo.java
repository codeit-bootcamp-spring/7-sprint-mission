package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelInfo {

    private final String channelAdmin;
    private final String channelName;
    private final String channelType;
    private final Long createdAt;
    private final List<String> members;

    public ChannelInfo(Channel channel) {
        this.channelAdmin = channel.getChannelAdmin().getUserName();
        this.channelName = channel.getChannelName();
        this.channelType = channel.getType().getDescType();
        this.createdAt = channel.getCreatedAt();
        this.members = channel.getMembers().stream().map(User::getUserName)
                .collect(Collectors.toList());
    }

    public String toString() {

        return channelName + '{' +
                " 관리자: " + channelAdmin +
                ", 채널유형: " + channelType +
                ", 생성일자: " + createdAt +
                ", 맴버: " + members +
                " }";
    }
}
