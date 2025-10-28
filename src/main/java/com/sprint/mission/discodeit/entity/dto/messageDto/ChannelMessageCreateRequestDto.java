package com.sprint.mission.discodeit.entity.dto.messageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ChannelMessageCreateRequestDto {

    @NonNull
    private final UUID authorId;
    @NonNull
    private final UUID channelId;
    private String content;

    private final List<AttachmentDto> files;

}
