package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enum_.ChannelType;

import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(

    UUID id,// 채널 ID
    Instant createdAt,
    Instant updatedAt,
    ChannelType type, //채널 타입
    String name, //채널 이름
    String description, //채널 설명
    List<UUID> participantIds,
    Instant lastMessageAt // 메시지 시간 정보

) {

  public static ChannelResponseDto from(Channel channel, List<UUID> participantIds,
      Instant lastMessageAt) {
    return new ChannelResponseDto(
        channel.getId(),
        channel.getCreatedAt(),
        channel.getUpdatedAt(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}
