package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;
public record MessageDto(
        UUID id,
        String content,
        UUID userId,
        UUID channelId,
        UUID binaryContentId
) {
    public static MessageDto from(Message m) {
        return new MessageDto(
                m.getId(),
                m.getContent(),
                m.getUser().getId(),
                m.getChannel().getId(),
                m.getBinaryContent() != null ? m.getBinaryContent().getId() : null
        );
    }
}
