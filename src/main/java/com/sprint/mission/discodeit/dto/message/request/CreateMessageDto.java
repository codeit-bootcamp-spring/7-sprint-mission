package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;

import java.util.List;
import java.util.UUID;

public record CreateMessageDto(
        String content,
        UUID userId,
        UUID channelId,
        List<CreateBinaryContentDto> createBinaryContentDtos
) {
}
