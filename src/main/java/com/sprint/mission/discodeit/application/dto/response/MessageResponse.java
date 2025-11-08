package com.sprint.mission.discodeit.application.dto.response;

import com.sprint.mission.discodeit.domain.Message;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;
@Getter
@Builder
public class MessageResponse {
    UUID messageId;
    String content;

    public static MessageResponse from(Message message){
        return MessageResponse.builder()
                .messageId(message.getImage())
                .content(message.getContent())
                .build();
    }
}
