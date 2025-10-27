package com.sprint.mission.discodeit.entity.dto.messageDto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DirectMessageCreateRequestDto {

    private UUID authorId;
    private UUID receiverId;
    private String content;

}
