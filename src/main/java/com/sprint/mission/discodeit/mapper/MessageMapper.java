package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;
    private final MessageAttachmentRepository messageAttachmentRepository;

    public MessageDto toDto(Message message){

        List<MessageAttachment> messageAttachmentList = message.getMessageAttachment();
        List<BinaryContentDto> binaryContentDtos = message.getAttachments()==null?List.of():messageAttachmentList.stream()
                .map(x-> binaryContentMapper.toDto(x.getBinaryContent())
        ).toList();

        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                userMapper.toDto(message.getAuthor()),
                binaryContentDtos
        );
    }

}
