package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Dto_Channel( //all private final
       @NotBlank(message = "channelId is mandatory")
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String channelName,
        String description,
        ChannelType channelType

//        List<UUID> userIDs; // PRIVATE 일 경우 사용
//        private Instant lastMessageAt; // [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
) {
    public static Dto_Channel from(Channel channel) {
        return Dto_Channel.builder()
                .id(channel.getId())
                .createdAt(channel.getCreatedAt())
                .updatedAt(channel.getUpdatedAt())
                .channelName(channel.getChannelName())
                .description(channel.getDescription())
                .channelType(channel.getChannelType())
                .build();
    }

    public static Dto_Channel fromPublic(Res_Channel resChannel) {
        return Dto_Channel.builder()
                .id(resChannel.channelId())
                .createdAt(resChannel.createdAt())
                .updatedAt(resChannel.updatedAt())
                .channelName(resChannel.channelName())
                .description(resChannel.description())
                .channelType(resChannel.channelType())
                .build();
    }

    public static Dto_Channel fromPrivate(Res_Channel resChannel) {
        return Dto_Channel.builder()
                .id(resChannel.channelId())
                .createdAt(resChannel.createdAt())
                .updatedAt(resChannel.updatedAt())
//                .channelName(resChannel.channelName())
//                .description(resChannel.description())
                .channelType(resChannel.channelType())
                .build();
    }
}
