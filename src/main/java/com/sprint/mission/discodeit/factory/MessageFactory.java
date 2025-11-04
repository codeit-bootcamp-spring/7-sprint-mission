package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public class MessageFactory {
    private MessageFactory(){}

    public static Message create(MessageCreateReq req, List<UUID> attachmentIds){
        return Message.builder()
                .channelId(req.channelId())
                .speakerId(req.speakerId())
                .content(req.content())
                .attachmentIds(attachmentIds)
                .build();
    }
}
