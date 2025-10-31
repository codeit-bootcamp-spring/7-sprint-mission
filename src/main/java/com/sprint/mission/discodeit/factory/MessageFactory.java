package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.entity.Message;

public class MessageFactory {
    private MessageFactory(){}

    public static Message create(MessageCreateReq req){
        return Message.builder()
                .channelId(req.channelId())
                .speakerId(req.speakerId())
                .content(req.content())
                .attachmentIds(req.attachmentIds())
                .build();

    }
}
