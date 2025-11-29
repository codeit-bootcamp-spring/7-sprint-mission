package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.mapper.dto.AttachmentsDto;
import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageMapper {
    private final MessageAttachmentsMapper attachmentsMapper;
    private final BinaryContentsRepository binaryContentsRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    public MessageDto toDto(Message message) {
        UserDto author = userMapper.toDto(message.getAuthor());

        List<MessageAttachments> messageAttachmentList = message.getMessageAttachmentList();

        List<BinaryContent> binaryContentList = binaryContentsRepository.findAllById(
            messageAttachmentList.stream()
                .map(binaryContent -> binaryContent.getBinaryContent().getId()).toList());

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
