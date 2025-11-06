package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public class MessageFactory {
    private MessageFactory(){}

    public static Message create(UUID speakerId, UUID channelId, MessageCreateReq req, List<UUID> attachmentIds){
        return Message.createWithAttachment(
                channelId,
                speakerId,
                req.content(),
                attachmentIds
        );
    }
}
