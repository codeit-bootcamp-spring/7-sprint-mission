package com.sprint.mission.discodeit.entity.dto.messageDto;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record MessageResponseDto(UUID id, String authorName, String content,
                                 Instant createdAt, Instant updatedAt, List<UUID> attachmentIds) {

    public static MessageResponseDto from(Message message) {

        return MessageResponseDto.builder()
                .id(message.getId())
                .authorName(message.getAuthor().getUserName())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .attachmentIds(message.getAttachmentIds())
                .build();
    }

    @Override
    public String toString() {

        String editSign = createdAt.equals(updatedAt) ? "" : " (수정됨)";
        return authorName + "   " + createdAt + "\n" + content + editSign;
    }
}
