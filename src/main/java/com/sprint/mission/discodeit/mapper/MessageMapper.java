package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.mapper.dto.AttachmentsDto;
import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import com.sprint.mission.discodeit.repository.jpa.MessageAttachmentsRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageMapper {
    private final MessageAttachmentsMapper attachmentsMapper;
    private final BinaryContentsRepository binaryContentsRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;
    private final MessageAttachmentsRepository messageAttachmentsRepository;

    public MessageDto toDto(Message message) {
        UserDto author = userMapper.toDto(message.getAuthor());

        List<MessageAttachments> messageAttachmentsList = messageAttachmentsRepository.findByMessageIs(message);
        List<UUID> binaryContentIdList  = messageAttachmentsList
            .stream()
            .map(messageAttachments -> messageAttachments.getBinaryContent().getId())
            .toList();

        List<BinaryContent> binaryContentList = binaryContentsRepository.findAllById(binaryContentIdList);

        List<BinaryContentDto> binaryContentDtoList = binaryContentList.stream().map(binaryContentMapper::toDto)
            .toList();

        return MessageDto.builder()
            .id(message.getId())
            .createdAt(message.getCreatedAt())
            .updatedAt(message.getUpdatedAt())
            .content(message.getContent())
            .channelId(message.getChannel().getId())
            .author(author)
            .attachments(binaryContentDtoList)
            .build();
    }
}
