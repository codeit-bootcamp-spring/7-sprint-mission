package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Message;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Builder
public record Res_Message( //all private final
    @NotBlank(message = "messageId is mandatory")
    UUID messageId,
    Instant createdAt,
    Instant updatedAt,

    UUID channelId,
    UUID authorId,
    List<UUID>attachemntIds,
    String message
) {
    public static Res_Message from(Message message) {
        return Res_Message.builder()
                .messageId(message.getId())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .channelId(message.getChannelId())
                .authorId(message.getAuthorId())
                .attachemntIds(message.getAttachemntIds())
                .message(message.getMessage())
                .build();
    }
}
