package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageResponseDto (
        String content, // 메시지
        UUID messageId, // 메시지 id
        UUID authorId, // 유저ID
        UUID channelId, // 채널ID
        List<UUID> attachmentIds // 첨부파일
) {
    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getContent(),
                message.getId(),
                message.getAuthorId(),
                message.getChannelId(),
                message.getAttachmentIds()
        );
    }
}

