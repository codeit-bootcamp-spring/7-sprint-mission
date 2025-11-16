package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseV2(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds
) {
    public static MessageResponseV2 toDto(Message message) {
        return new MessageResponseV2(
                message.getUuid(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getReceiver().getUuid(),
                message.getSender().getUuid(),
                message.getAttachments().stream()
                        .map(BinaryContent::getUuid)
                        .toList()
        );
    }
}
