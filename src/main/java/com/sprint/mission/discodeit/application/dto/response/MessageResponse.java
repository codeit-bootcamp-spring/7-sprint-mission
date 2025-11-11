package com.sprint.mission.discodeit.application.dto.response;

import com.sprint.mission.discodeit.domain.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        Instant createdAt,
        Instant updateAt,
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds
) {
    public static MessageResponse from(Message message){
        return new MessageResponse(message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getAttachmentIds());
    }
}
