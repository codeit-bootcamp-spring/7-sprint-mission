package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.enums.ReceiverType;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record GetMessageDto(
        String senderId,
        ReceiverType type,
        String receiverId
) {
    public GetMessageDto {
        if (senderId() == null && receiverId == null)
            throw new IllegalArgumentException("발신자 또는 수신자 둘 중 하나는 필수입니다.");
        if (receiverId() != null && type == null)
            throw new IllegalArgumentException("수신자로 조회하는 경우 타입이 필수입니다.");
    }
}
