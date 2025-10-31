package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateReq;

import java.util.List;
import java.util.UUID;

public record MessageCreateReq (
        UUID channelId,
        UUID speakerId,
        String content,
        List<BinaryContentCreateReq> attachmentIds
){
    public MessageCreateReq {
        if (attachmentIds == null) {
            attachmentIds = List.of();
        }
    }
}
