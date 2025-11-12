package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record MessageResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds
) {

  public static MessageResponseDto from(Message message) {
    return MessageResponseDto.builder()
        .id(message.getId())
        .createdAt(message.getCreateAt())
        .updatedAt(message.getUpdateAt())
        .content(message.getContent())
        .channelId(message.getChannelId())
        .authorId(message.getUserId())
        .attachmentIds(message.getAttachmentIds())
        .build();
  }
}
