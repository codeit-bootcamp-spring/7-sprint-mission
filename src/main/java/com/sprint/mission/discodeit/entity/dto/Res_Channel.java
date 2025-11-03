package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_Channel(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String channelName,
        ChannelType channelType,
        String description
) {
    public static Res_Channel from(Channel channel) {
        return Res_Channel.builder()
                .id(channel.getId())
                .createdAt(channel.getCreatedAt())
                .updatedAt(channel.getUpdatedAt())
                .channelName(channel.getChannelName())
                .channelType(channel.getChannelType())
                .description(channel.getDescription())
                .build();
    }

//    public static Res_Channel from(Channel channel, Dto_ChannelUpdate dtoChannelUpdate) {
//        return Res_Channel.builder()
//                .id(channel.getId())
//                .createdAt(channel.getCreatedAt())
//                .updatedAt(channel.getUpdatedAt())
//                .channelName(dtoChannelUpdate.channelName())
//                .channelType(dtoChannelUpdate.channelType())
//                .description(dtoChannelUpdate.descriptio())
//                .build();
//    }
}
