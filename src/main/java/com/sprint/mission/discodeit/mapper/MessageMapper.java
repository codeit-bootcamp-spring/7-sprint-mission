package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    private BinaryContentMapper binaryContentMapper;
    private UserMapper userMapper;

    public MessageDto toDto(Message message) {
        UserDto author = userMapper.toDto(message.getAuthor());

        List<BinaryContentDto> attachments = message.getAttachments().stream()
            .map(binaryContentMapper::toDto)
            .toList();

        return MessageDto.builder()
            .id(message.getId())
            .createdAt(message.getCreatedAt())
            .updatedAt(message.getUpdatedAt())
            .content(message.getContent())
            .channelId(message.getChannel().getId())
            .author(author)
            .attachments(attachments)
            .build();
    }
}
