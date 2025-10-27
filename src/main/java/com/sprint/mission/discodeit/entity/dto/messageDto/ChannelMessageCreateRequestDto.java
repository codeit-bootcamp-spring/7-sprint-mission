package com.sprint.mission.discodeit.entity.dto.messageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ChannelMessageCreateRequestDto {

    @NonNull
    private UUID authorId;
    @NonNull
    private UUID channelId;
    private String content;

}
