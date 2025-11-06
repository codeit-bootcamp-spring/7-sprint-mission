package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;

import java.util.List;
import java.util.UUID;

public record MessageUpdateReq(
        String content,
        List<UUID> keepAttachmentIds,
        List<BinaryContentCreateReq> newAttachmentReqs
){
    public MessageUpdateReq {
        if (newAttachmentReqs == null) {
            newAttachmentReqs = List.of();
        }
        if(keepAttachmentIds == null){
            keepAttachmentIds = List.of();
        }
    }

    public static MessageUpdateReq from(MessageInfoReq infoReq,
                                        List<UUID> keepAttachmentIds,
                                        List<BinaryContentCreateReq> newAttachmentReqs){
        return new MessageUpdateReq(
                infoReq.content(),
                keepAttachmentIds,
                newAttachmentReqs);
    }
}
