package com.sprint.mission.discodeit.dto.mapper;

import com.sprint.mission.discodeit.dto.entity.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserMapper userMapper;

    public MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                userMapper.toDto(message.getAuthor()),
                message.getAttachments() == null ? null :
                        message.getAttachments().stream()
                                .map(BinaryContentMapper::toDto)
                                .toList()
        );
    }
}
