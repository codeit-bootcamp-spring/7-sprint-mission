package com.sprint.mission.discodeit.entity.dto.channelDto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

@Getter
public class ChannelInfo {

    private final UUID id;
    private final String channelAdmin;
    private final String channelName;
    private final String channelType;
    private final String createdAt;
    private final String updatedAt;
    private final List<String> members;

    public ChannelInfo(Channel channel) {
        this.id = channel.getId();
        this.channelAdmin = channel.getChannelAdmin().getUserName();
        this.channelName = channel.getChannelName();
        this.channelType = channel.getType().getDescType();
        this.createdAt = changeTime(channel.getCreatedAt());
        this.updatedAt = changeTime(channel.getUpdatedAt());
        this.members = channel.getMembers().stream().map(User::getUserName)
                .collect(Collectors.toList());
    }

    public String toString() {

        return channelName + '[' +
                "관리자: " + channelAdmin +
                ", 채널유형: " + channelType +
                ", 생성일자: " + createdAt +
                ", 맴버: " + members +
                "]";
    }
}
