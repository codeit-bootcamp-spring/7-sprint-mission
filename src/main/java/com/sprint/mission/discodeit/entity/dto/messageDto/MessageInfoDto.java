package com.sprint.mission.discodeit.entity.dto.messageDto;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.binaryContent.BinaryContent;
import lombok.Builder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

@Builder
public record MessageInfoDto(UUID id, String authorName, String content,
                             Instant createdAt, Instant updatedAt, List<UUID> attachmentIds) {

    public static MessageInfoDto from(Message message, List<BinaryContent> files) {

        // 첨부파일 선택
        List<UUID> fileIds = null;
        if (files != null && !files.isEmpty()) {
            fileIds = files.stream().map(BinaryContent::getId).toList();
        }

        return MessageInfoDto.builder()
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
