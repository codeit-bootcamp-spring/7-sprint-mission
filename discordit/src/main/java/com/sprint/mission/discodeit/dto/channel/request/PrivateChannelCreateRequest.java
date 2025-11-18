package com.sprint.mission.discodeit.dto.channel.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;
import java.util.UUID;

@Schema(description = "비공개 채널 생성 요청")
public record PrivateChannelCreateRequest(
        @Schema(description = "참여자 ID 목록", example = "[\"123e4567-e89b-12d3-a456-426614174000\", \"123e4567-e89b-12d3-a456-426614174001\"]")
        Set<UUID> participantIds
) {
}
