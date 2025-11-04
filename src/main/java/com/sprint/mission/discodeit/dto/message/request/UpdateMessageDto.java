package com.sprint.mission.discodeit.dto.message.request;

import java.util.UUID;


public record UpdateMessageDto(
        UUID messageId,
        String content
){

}
