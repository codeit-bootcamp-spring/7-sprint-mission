package com.sprint.mission.discodeit.dto.messageDto;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(UUID id,
                         Instant createdAt,
                         Instant updatedAt,
                         String content,
                         UUID channelId,
                         UserDto author,
                         List<BinaryContentDto> attachments
) {
    public static MessageDto from(Message message, List<BinaryContentDto> attachments) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                UserDto.from(message.getAuthor(),
                        message.getAuthor().getStatus().isOnline()),
                attachments
        );
    }

}
