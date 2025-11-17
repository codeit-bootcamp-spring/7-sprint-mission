package com.sprint.mission.discodeit.entity.dto.messageDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MessageCreateRequest {

    private String content;
    @NotNull
    private UUID authorId;
    @NotNull
    private UUID channelId;
}
