package com.sprint.mission.discodeit.dto.entity.channel.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "채널 수정 요청")
public record ChannelUpdateRequest(
        @Schema(description = "새 채널 이름", example = "새로운 채널명")
        @NotBlank(message = "채널 이름은 비어있을 수 없습니다.")
        String newName,
        @Schema(description = "새 채널 설명", example = "수정된 채널 설명입니다", nullable = true)
        String newDescription
) {
}
