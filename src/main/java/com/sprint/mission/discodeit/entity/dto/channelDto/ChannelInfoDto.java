package com.sprint.mission.discodeit.entity.dto.channelDto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

@Builder
public record ChannelInfoDto(UUID id, String channelName, String channelAdmin, String channelType, String createdAt,
                             String updatedAt, List<String> members) {

    public static ChannelInfoDto from(Channel channel) {

        return ChannelInfoDto.builder()
                .id(channel.getId())
                .channelName(channel.getChannelName())
                .channelAdmin(channel.getChannelAdmin().getUserName())
                .channelType(channel.getType().toString())
                .createdAt(changeTime(channel.getCreatedAt()))
                .updatedAt(changeTime(channel.getUpdatedAt()))
                .members(channel.getMembers().stream().map(User::getUserName)
                        .collect(Collectors.toList()))
                .build();
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
