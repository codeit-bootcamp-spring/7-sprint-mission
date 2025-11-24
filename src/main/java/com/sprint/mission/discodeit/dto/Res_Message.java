package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;



public record Res_Message( //all private final
    //@NotBlank(message = "messageId is mandatory")
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds
    ) {
    public static Res_Message from(Message message) {
        return new Res_Message(
            message.getId(),
            message.getCreatedAt(),
            message.getUpdatedAt(),
            message.getContent(),
            message.getChannelId(),
            message.getAuthorId(),
            message.getAttachments()
        );
    }
}
