package com.sprint.mission.discodeit.dto.request.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageCreateRequestDto {
    private final String content;
    private final String userName;
    private final UUID authorId;
    private final UUID channelId;
    private final List<UUID> attachmentIds;
}
