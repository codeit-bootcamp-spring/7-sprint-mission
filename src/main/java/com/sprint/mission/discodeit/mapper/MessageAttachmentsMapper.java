package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.dto.dto_Neo.MessageAttachmentsDto;
import org.springframework.stereotype.Component;

@Component
public class MessageAttachmentsMapper {

    public MessageAttachmentsDto toDto(MessageAttachments attachments) {
        return MessageAttachmentsDto.builder()
            .id(attachments.getId())
            .messageId(attachments.getMessage().getId())
            .attachmentId(attachments.getBinaryContent().getId())
            .build();
    }
}
