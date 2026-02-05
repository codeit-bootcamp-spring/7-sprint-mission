package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageUpdateCommand(
        UUID messageId,
        String content
) {

    public static MessageUpdateCommand from(MessageUpdateRequestDto requestDto, UUID messageId) {
        return new MessageUpdateCommand(
                messageId,
                requestDto.newContent()
        );
    }
}
