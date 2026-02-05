package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.messageDto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;
    private final AuthService authService;

    public MessageDto toDto(Message message) {

        if (message.getAuthor() == null)
            return null;

        boolean isOnline = authService.isOnline(message.getAuthor().getId());

        return new MessageDto(

                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                userMapper.toDto(message.getAuthor(), isOnline),
                message.getAttachments().stream()
                        .map(binaryContentMapper::toDto).collect(Collectors.toList())
        );
    }
}

