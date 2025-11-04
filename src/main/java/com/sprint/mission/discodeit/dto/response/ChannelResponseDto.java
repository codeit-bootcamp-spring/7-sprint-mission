package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enum_.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto (

    UUID channelId,// 채널 ID
    String channelName, //채널 이름
    String description, //채널 설명
    ChannelType channelType, //채널 타입
    List<UUID> members, // 유저
    Instant readMessage // 메시지 시간 정보

) {
    public static ChannelResponseDto from(Channel channel, Instant readMessage, List<UUID> members) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getType(),
                channel.getMembers(),
                readMessage
        );
    }
}
