package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Dto_Message(
    UUID channelId,
    UUID authorid,
    String message
) {
    public static Dto_Message from(UUID channelId, UUID authorid, String message) {
        return Dto_Message.builder()
                .channelId(channelId)
                .authorid(authorid)
                .message(message)
                .build();
    }
}

