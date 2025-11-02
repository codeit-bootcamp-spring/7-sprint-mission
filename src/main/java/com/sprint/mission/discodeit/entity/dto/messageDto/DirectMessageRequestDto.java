package com.sprint.mission.discodeit.entity.dto.messageDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class DirectMessageRequestDto {
    @NonNull
    private final UUID authorId;
    @NonNull
    private final UUID receiverId;
    private String content;

    private List<AttachmentDto> files;
}
