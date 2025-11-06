package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.entity.ReceiveType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateMessageRequestDto {
    private final UUID senderId;
    private final UUID receiverId;
    private final String content;
    private final ReceiveType receiveType;
}
