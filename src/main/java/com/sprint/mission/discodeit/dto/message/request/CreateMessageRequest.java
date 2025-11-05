package com.sprint.mission.discodeit.dto.message.request;

import java.util.List;
import java.util.UUID;

public record CreateMessageRequest(
        UUID authorId,
        UUID channelId,
        String content,
       List<byte[]> attachment

) {
}
