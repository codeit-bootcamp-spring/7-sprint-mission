package com.sprint.mission.discodeit.dto.message.request;

import java.util.List;
import java.util.UUID;

public record MessageCreateReq (
        UUID channelId,
        UUID speakerId,
        String content,
        List<UUID> attachmentIds
){
    public MessageCreateReq {
        if (attachmentIds == null) {
            attachmentIds = List.of();
        }
    }
}
