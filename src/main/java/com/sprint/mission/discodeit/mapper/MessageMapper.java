package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    public MessageResponseDto toDto(Message message, boolean authorOline) {
        if (message == null) {
            throw new IllegalArgumentException("Message is null");
        }

        UserResponseDto authorDto = null;
        if (message.getAuthor() != null) {
            authorDto = userMapper.toDto(message.getAuthor(), authorOline);
        }

        List<BinaryContentResponseDto> attachmentDtos
                = binaryContentMapper.toDtoList(message.getAttachments());

        return new MessageResponseDto(
                message.getId(),
                message.getContent(),
                authorDto,
                message.getChannel().getId(),
                attachmentDtos,
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }

    public List<MessageResponseDto> toDtoList(List<Message> messages, Map<UUID, Boolean> authorOlineMap) {
        if (messages == null || messages.isEmpty()) {
            return List.of();
        }
        return messages.stream()
                .map(message -> {
                    UUID authorId = message.getAuthor() != null
                            ? message.getAuthor().getId() : null;
                    boolean online = authorId != null
                            && Boolean.TRUE.equals(authorOlineMap.get(authorId));

                    return toDto(message, online);
                })
                .toList();
    }
}
