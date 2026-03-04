package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import lombok.Getter;

@Getter
public class MessageCreatedEvent {
    MessageResponseDto messageResponseDto;
    String channelName;

    public MessageCreatedEvent(MessageResponseDto messageResponseDto, String channelName) {
        this.messageResponseDto = messageResponseDto;
        this.channelName = channelName;
    }
}
