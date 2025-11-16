package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.enums.ReceiverType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Schema(description = "메시지 생성 요청")
public record MessageCreateRequestV2(
        @Schema(description = "작성자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "보내는 사람은 필수입니다.")
        UUID authorId,
        @Schema(description = "채널 ID", example = "123e4567-e89b-12d3-a456-426614174001")
        @NotNull(message = "수신자는 필수입니다")
        UUID channelId,
        @Schema(description = "메시지 내용", example = "안녕하세요!")
        @NotNull(message = "메세지는 필수입니다.")
        String content
) {
}
