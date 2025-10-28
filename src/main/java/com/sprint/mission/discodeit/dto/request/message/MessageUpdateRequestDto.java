package com.sprint.mission.discodeit.dto.request.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageUpdateRequestDto {
    private final String content;
    private final String userName;
    private final UUID authorId;
    private final UUID channelId;
    private final UUID messageId;
    private final List<UUID> attachmentIds;
    private final boolean isDeleted;
}
