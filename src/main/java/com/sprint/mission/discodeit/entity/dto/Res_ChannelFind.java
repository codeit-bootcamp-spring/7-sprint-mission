package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record Res_ChannelFind(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String channelName,
        String description,
        ChannelType channelType,

//        [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
//        [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
        Instant lastMessageAt,
        List<UUID> privateChannelUserIDs
) {
    public static Res_ChannelFind from(Dto_Channel channel, Instant lastMessageAt, List<UUID> privateChannelUserIDs) {
        return Res_ChannelFind.builder()
                .id(channel.id())
                .createdAt(channel.createdAt())
                .updatedAt(channel.updatedAt())
                .channelName(channel.channelName())
                .description(channel.description())
                .channelType(channel.channelType())
                .lastMessageAt(lastMessageAt)
                .privateChannelUserIDs(privateChannelUserIDs)
                .build();
    }
}
