package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record MessageResponseDto(
        UUID messageId,
        Instant updateAt,
        String content,
        UUID channelId,
        UUID userId,
        List<UUID> attachmentIds
) {
    public static MessageResponseDto from(Message message) {
        return MessageResponseDto.builder()
                .messageId(message.getId())
                .updateAt(message.getUpdateAt())
                .content(message.getContent())
                .channelId(message.getChannelId())
                .userId(message.getUserId())
                .attachmentIds(message.getAttachmentIds())
                .build();
    }
}
