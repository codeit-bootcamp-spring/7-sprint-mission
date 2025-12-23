package com.sprint.mission.discodeit.dto.mapper;

import com.sprint.mission.discodeit.dto.entity.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public static MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                UserMapper.toDto(message.getAuthor()),
                message.getAttachments() == null ? null :
                        message.getAttachments().stream()
                                .map(BinaryContentMapper::toDto)
                                .toList()
        );
    }
}
