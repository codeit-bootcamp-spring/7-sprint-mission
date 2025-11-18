package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.enums.ReceiverType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MessageCreateRequest(
        @NotNull(message = "보내는 사람은 필수입니다.")
        String senderUserId,
        @NotNull(message = "수신 타입은 필수입니다.")
        ReceiverType type,
        @NotNull(message = "수신자는 필수입니다")
        String receiverId,
        @NotNull(message = "메세지는 필수입니다.")
        String message,
        List<String> fileUrls
) {
}
