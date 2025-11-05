package com.sprint.mission.discodeit.dto.response.message;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        String content,
        String userName,
        UUID authorId,
        UUID channelId,
        List<UUID> attachmentIds,
        boolean isDeleted) {

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getContent(),
                message.getUserName(),
                message.getAuthorId(),
                message.getChannelId(),
                message.getAttachmentIds(),
                message.isDeleted()
        );
    }
}
