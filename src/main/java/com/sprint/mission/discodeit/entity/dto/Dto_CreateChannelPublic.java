package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@Builder
public record Dto_CreateChannelPublic(
//        UUID userId,
//        Instant createdAt,
//        Instant updatedAt, // 유닉스 타임스탬프
        ChannelType channelType,
        String channelName,
        String description
) {
    public static Dto_CreateChannelPublic from(String channelName, String description) {
        return Dto_CreateChannelPublic.builder()
//                .userId(UUID.randomUUID())
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
                .channelName(channelName)
                .description(description)
                .channelType(PUBLIC)
                .build();
    }
}
//{
//    "channelType" : "",
//    "channelName" : "",
//    "description" : ""
//}
