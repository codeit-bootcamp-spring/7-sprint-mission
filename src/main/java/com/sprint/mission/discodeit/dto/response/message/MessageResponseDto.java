package com.sprint.mission.discodeit.dto.response.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageResponseDto {
    private final String content;
    private final String userName;
    private final UUID authorId;
    private final UUID channelId;
    private final List<UUID> attachmentIds;
    private final boolean isDeleted;
}
