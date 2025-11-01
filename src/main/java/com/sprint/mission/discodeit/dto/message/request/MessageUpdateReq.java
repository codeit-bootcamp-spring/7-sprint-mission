package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentUpdateReq;

import java.util.List;

public record MessageUpdateReq(
        String content,
        List<BinaryContentUpdateReq> attachmentReqs
){
    public MessageUpdateReq {
        if (attachmentReqs == null) {
            attachmentReqs = List.of();
        }
    }
}
