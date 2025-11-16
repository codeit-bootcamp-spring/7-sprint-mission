package com.sprint.mission.discodeit.dto.channel.request;

import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema(description = "공개 채널 생성 요청")
public record PublicChannelCreateRequest(
        @Schema(description = "채널 이름", example = "일반 채팅방")
        @NotNull(message = "채널이름은 필수입니다.")
        String name,
        @Schema(description = "채널 설명", example = "자유롭게 대화하는 채널입니다", nullable = true)
        String description
) {
}
