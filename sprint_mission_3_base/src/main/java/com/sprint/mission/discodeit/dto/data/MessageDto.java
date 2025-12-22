package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record MessageDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UserDto author,
        List<BinaryContentDto> attachments
) {
    public static MessageDto from(Message message) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                UserDto.from(message.getAuthor()),
                message.getAttachments() == null
                        ? List.of()
                        : message.getAttachments().stream()
                        .map(BinaryContentDto::from)
                        .collect(Collectors.toList())
        );
    }
}
