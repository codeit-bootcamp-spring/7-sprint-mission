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

    public static MessageResponseDto from(Message message, List<BinaryContent> files) {

        // 첨부파일 선택
        List<UUID> fileIds = null;
        if (files != null && !files.isEmpty()) {
            fileIds = files.stream().map(BinaryContent::getId).toList();
        }

        return MessageResponseDto.builder()
                .id(message.getId())
                .authorName(message.getAuthor().getUserName())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .attachmentIds(fileIds)
                .build();
    }

    @Override
    public String toString() {

        String editSign = createdAt.equals(updatedAt) ? "" : " (수정됨)";
        return authorName + "   " + createdAt + "\n" + content + editSign;
    }
}
