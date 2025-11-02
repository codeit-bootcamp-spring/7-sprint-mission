package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_Message(
    UUID id,
    Instant createdAt,
    Instant updatedAt,

    UUID channelId,
    UUID authorId,
    List<UUID>attachemntIds,
    String message
) {
    public static Res_Message from(Message message) {
        return Res_Message.builder()
                .id(message.getId())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .channelId(message.getChannelId())
                .authorId(message.getAuthorId())
                .attachemntIds(message.getAttachemntIds())
                .message(message.getMessage())
                .build();
    }
}
