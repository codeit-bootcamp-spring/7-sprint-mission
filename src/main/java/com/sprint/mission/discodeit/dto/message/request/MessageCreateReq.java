package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;

import java.util.List;

public record MessageCreateReq (
        String content,
        List<BinaryContentCreateReq> attachmentIds
){
    public MessageCreateReq {
        if (attachmentIds == null) {
            attachmentIds = List.of();
        }
    }
}
