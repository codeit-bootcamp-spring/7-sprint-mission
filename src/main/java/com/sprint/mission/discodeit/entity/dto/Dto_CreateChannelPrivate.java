package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record Dto_CreateChannelPrivate(
//        ChannelType channelType,
        List<UUID> userIDs // PRIVATE 일 경우 사용
) {
    public static Dto_CreateChannelPrivate from(List<UUID> userIDs) {
//        [ ] name과 description 속성은 생략합니다.
        return Dto_CreateChannelPrivate.builder()
//                .userId(UUID.randomUUID())
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .channelName(channelName)
//                .description(description)
//                .channelType(PRIVATE)
                .userIDs(userIDs)
                .build();
    }
}
