package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.response.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    public MessageDto toDto(Message entity) {
        if (entity == null) return null;


        List<BinaryContentDto> attachments = entity.getAttachments() == null
                ? List.of()
                : entity.getAttachments().stream()
                .map(MessageAttachment::getAttachment)
                .map(binaryContentMapper::toDto)
                .toList();


        return new MessageDto(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getContent(),
                entity.getChannel().getId(),
                userMapper.toDto(entity.getAuthor()),
                attachments
        );
    }
}