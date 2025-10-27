package com.sprint.mission.discodeit.entity.dto.messageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DirectMessageCreateRequestDto {
    @NonNull
    private UUID authorId;
    @NonNull
    private UUID receiverId;
    private String content;

}
