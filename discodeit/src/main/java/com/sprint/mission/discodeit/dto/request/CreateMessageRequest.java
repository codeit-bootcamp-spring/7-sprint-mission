package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CreateMessageRequest {
    private String content;
    private UUID channelId;
    private UUID authorId;
}
