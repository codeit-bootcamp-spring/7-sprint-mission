package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageCreatedEvent extends ApplicationEvent {
    MessageResponseDto messageResponseDto;
    String channelName;

    public MessageCreatedEvent(Object source, MessageResponseDto messageResponseDto, String channelName) {
        super(source);
        this.messageResponseDto = messageResponseDto;
        this.channelName = channelName;
    }
}
